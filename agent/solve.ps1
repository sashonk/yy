# solve.ps1 - automated level solving for YinYang via dev-agent (HTTP :8765)
# Usage:
#   powershell -NoProfile -ExecutionPolicy Bypass -File agent\solve.ps1 -From 1 -To 5
# Params:
#   -BaseUrl     dev-agent base url (default http://127.0.0.1:8765)
#   -From/-To    level range (ignored when -Levels is given)
#   -Levels      explicit list of levels, e.g. -Levels 1,4,13,21,29
#   -ForceSolve  ignore saved solutions, recompute with BFS
#   -NoSave      do not update walkthrough.json
#   -MaxStates   BFS state limit (default 200000)
#   -MoveDelayMs pause after each move (default 150 ms)
#   -LevelDelaySec pause on the win screen before the next level (default 6 s)
# NOTE: keep this file ASCII-only: Windows PowerShell 5.1 reads BOM-less
# .ps1 files as ANSI and non-ASCII text breaks the parser.

param(
    [string]$BaseUrl = 'http://127.0.0.1:8765',
    [int]$From = 1,
    [int]$To = 5,
    [object[]]$Levels = @(),
    [switch]$ForceSolve,
    [switch]$NoSave,
    [int]$MaxStates = 200000,
    [int]$MoveDelayMs = 150,
    [int]$LevelDelaySec = 6
)

if ($Levels.Count -gt 0) {
    # accept both int arrays and comma-separated strings (e.g. -Levels "1,4,13")
    $levelList = @()
    foreach ($e in $Levels) {
        if ($e -is [string]) {
            foreach ($part in ($e -split ',')) {
                $t = $part.Trim()
                if ($t) { $levelList += [int]$t }
            }
        }
        else { $levelList += [int]$e }
    }
}
else {
    $levelList = @($From..$To)
}

$ErrorActionPreference = 'Stop'
$Root   = Split-Path -Parent $MyInvocation.MyCommand.Path
$LogPath = Join-Path $Root 'last_run.log'
$WtPath  = Join-Path $Root 'walkthrough.json'

# ---------- logging ----------
function Log([string]$msg) {
    $line = '[{0}] {1}' -f (Get-Date -Format 'yyyy-MM-dd HH:mm:ss'), $msg
    Write-Host $line
    Add-Content -Path $LogPath -Value $line -Encoding UTF8
}
Set-Content -Path $LogPath -Value ("=== run {0}: levels {1} ===" -f (Get-Date -Format 'yyyy-MM-dd HH:mm:ss'), ($levelList -join ',')) -Encoding UTF8

# ---------- HTTP ----------
function Get-State {
    Invoke-RestMethod -Uri "$BaseUrl/state" -Method Get -TimeoutSec 10
}
function Send-Move([string]$dir) {
    Invoke-RestMethod -Uri "$BaseUrl/move?dir=$dir" -Method Post -TimeoutSec 15 | Out-Null
}
function Send-Click([string]$name) {
    Invoke-RestMethod -Uri "$BaseUrl/click?name=$name" -Method Post -TimeoutSec 15 | Out-Null
}
function Send-Start([int]$lvl) {
    Invoke-RestMethod -Uri "$BaseUrl/start?level=$lvl" -Method Post -TimeoutSec 20 | Out-Null
}
function Wait-NotBusy([int]$timeoutSec = 30) {
    $deadline = (Get-Date).AddSeconds($timeoutSec)
    while ((Get-Date) -lt $deadline) {
        $s = Get-State
        if (-not $s.busy) { return $s }
        Start-Sleep -Milliseconds 150
    }
    return (Get-State)
}
function Wait-Won([int]$timeoutSec = 90) {
    $deadline = (Get-Date).AddSeconds($timeoutSec)
    while ((Get-Date) -lt $deadline) {
        $s = Get-State
        if ($s.won) { return $s }
        Start-Sleep -Milliseconds 250
    }
    return (Get-State)
}

# ---------- game simulation ----------
# grid item codes: 1=Wall, 2=Water, 3=Ground, 4=Box, 5=Metal, 6=Fall
# Water is a Box subclass in the game; Fall is a Box that drops under gravity
# Yang and Mirror are "figures" (they move); Mirror moves opposite to input
$ItemCode = @{ Wall = 1; Water = 2; Ground = 3; Box = 4; Metal = 5; Fall = 6 }
$DirStep    = @{ up = @(0, 1);  down = @(0, -1); left = @(-1, 0); right = @(1, 0) }
$DirCounter = @{ up = 'down'; down = 'up'; left = 'right'; right = 'left' }

function New-SimState($json) {
    $grid  = @{}
    $spots = @{}
    $ys    = @()
    foreach ($c in $json.cells) {
        $k = "$($c.x),$($c.y)"
        if ($c.item -eq 'Yang')        { $ys += , @([int]$c.x, [int]$c.y, 0, 0) }
        elseif ($c.item -eq 'Mirror')  { $ys += , @([int]$c.x, [int]$c.y, 0, 1) }
        elseif ($c.item)               { $grid[$k] = $ItemCode[$c.item] }
        if ($c.spot) { $spots[$k] = $true }
    }
    return @{ grid = $grid; ys = $ys; spots = $spots; rotten = [bool]$json.rotten }
}

# gravity: Fall items (code 6) drop down after each move (Game2:834-868)
function Invoke-FallGravity($state) {
    $falls = @()
    foreach ($k in $state.grid.Keys) {
        if ($state.grid[$k] -eq 6) { $falls += $k }
    }
    if ($falls.Count -eq 0) { return }
    # sort bottom-first (ascending y), as in Game2
    $sorted = @($falls | Sort-Object { [int]($_.Split(',')[1]) })
    $occ = @{}
    foreach ($y in $state.ys) { $occ["$($y[0]),$($y[1])"] = $true }
    $targets = @($sorted | ForEach-Object { $_ })
    for ($i = 0; $i -lt $sorted.Count; $i++) {
        $parts = $sorted[$i].Split(',')
        $x = [int]$parts[0]; $ty = [int]$parts[1]
        $c = 1
        while ($true) {
            $ny = $ty - $c
            $c++
            $nk = "$x,$ny"
            if (-not $state.grid.Contains($nk) -and -not $occ.Contains($nk) -and ($targets -notcontains $nk)) {
                $ty = $ny; $c = 1
            }
            else { break }
        }
        $targets[$i] = "$x,$ty"
    }
    for ($i = 0; $i -lt $sorted.Count; $i++) {
        if ($targets[$i] -ne $sorted[$i]) {
            $state.grid.Remove($sorted[$i])
            $state.grid[$targets[$i]] = 6
        }
    }
}

function Invoke-SimStep($state, [string]$dir) {
    $grid = $state.grid.Clone()
    $ys   = @($state.ys | ForEach-Object { , ($_.Clone()) })
    $spots = $state.spots
    $d = $DirStep[$dir]

    # processing order: figures leading in move direction go first (as in Game2)
    $idx = @(0..($ys.Count - 1))
    switch ($dir) {
        'up'    { $idx = @($idx | Sort-Object { $ys[$_][1] } -Descending) }
        'down'  { $idx = @($idx | Sort-Object { $ys[$_][1] }) }
        'left'  { $idx = @($idx | Sort-Object { $ys[$_][0] }) }
        'right' { $idx = @($idx | Sort-Object { $ys[$_][0] } -Descending) }
    }

    $occ = @{}
    foreach ($y in $ys) { $occ["$($y[0]),$($y[1])"] = $true }

    $movedFrom = @()
    $anyMove = $false
    foreach ($i in $idx) {
        $y  = $ys[$i]
        $ed = if ($y[3]) { $DirStep[$DirCounter[$dir]] } else { $d }   # mirror moves back
        $sx = $y[0]; $sy = $y[1]
        $tx = $sx + $ed[0]; $ty = $sy + $ed[1]
        $sk = "$sx,$sy"; $tk = "$tx,$ty"
        if ($occ.Contains($tk)) { continue }          # cell taken by another figure

        $code = 0
        if ($grid.Contains($tk)) { $code = $grid[$tk] }
        $moved = $false

        if ($code -eq 0 -or $code -eq 2) {
            # empty cell or water (water is absorbed)
            if ($code -eq 2) { $grid.Remove($tk) }
            if ($y[2]) { $grid[$sk] = 3 } elseif ($grid.Contains($sk)) { $grid.Remove($sk) }
            $y[0] = $tx; $y[1] = $ty; $y[2] = 0
            $moved = $true
        }
        elseif ($code -eq 3) {
            # ground - picked up by the figure
            $grid.Remove($tk)
            if ($y[2]) { $grid[$sk] = 3 } elseif ($grid.Contains($sk)) { $grid.Remove($sk) }
            $y[0] = $tx; $y[1] = $ty; $y[2] = 1
            $moved = $true
        }
        elseif ($code -eq 4 -or $code -eq 6) {
            # box / water / fall - pushed if the cell behind it is free
            # (note: water absorbed branch above is unreachable for push; game
            #  treats Water as a pushable Box when approached from the side)
            $bk = "$($tx + $ed[0]),$($ty + $ed[1])"
            if (-not $grid.Contains($bk) -and -not $occ.Contains($bk)) {
                if ($y[2]) { $grid[$sk] = 3 } elseif ($grid.Contains($sk)) { $grid.Remove($sk) }
                $grid.Remove($tk)
                $grid[$bk] = $code
                $y[0] = $tx; $y[1] = $ty; $y[2] = 0
                $moved = $true
            }
        }
        # 1 (Wall) and 5 (Metal) - blocked, no move

        if ($moved) {
            $occ.Remove($sk)
            $occ[$tk] = $true
            $movedFrom += $sk
            $anyMove = $true
        }
    }

    if (-not $anyMove) { return $null }

    # rotten: walls grow on abandoned cells
    if ($state.rotten) {
        foreach ($sk in $movedFrom) {
            if (-not $grid.Contains($sk) -and -not $occ.Contains($sk) -and -not $spots.Contains($sk)) {
                $grid[$sk] = 1
            }
        }
    }
    $ns = @{ grid = $grid; ys = $ys; spots = $spots; rotten = $state.rotten }
    Invoke-FallGravity $ns
    return $ns
}

function Test-SimWin($state) {
    foreach ($y in $state.ys) {
        if (-not $state.spots.Contains("$($y[0]),$($y[1])")) { return $false }
    }
    return $true
}

function Get-SimKey($state) {
    if ($state.grid.Count -gt 0) {
        $gk = ($state.grid.Keys | Sort-Object | ForEach-Object { "$_=$($state.grid[$_])" }) -join ';'
    } else { $gk = '' }
    $yk = (@($state.ys | ForEach-Object { $_ -join ',' }) | Sort-Object) -join ';'
    return "$gk|$yk"
}

# ---------- solver (BFS) ----------
function Find-Solution($json, [int]$maxStates) {
    $init = New-SimState $json
    if (Test-SimWin $init) { return , @() }

    $visited = @{}
    $queue = New-Object System.Collections.Queue
    $queue.Enqueue(@{ s = $init; p = @() })
    $visited[(Get-SimKey $init)] = $true

    while ($queue.Count -gt 0) {
        $node = $queue.Dequeue()
        foreach ($dir in @('up', 'down', 'left', 'right')) {
            $ns = Invoke-SimStep $node.s $dir
            if ($null -eq $ns) { continue }
            $k = Get-SimKey $ns
            if ($visited.Contains($k)) { continue }
            if ($visited.Count -ge $maxStates) { return $null }
            $visited[$k] = $true
            $path = @($node.p) + $dir
            if (Test-SimWin $ns) { return , $path }
            $queue.Enqueue(@{ s = $ns; p = $path })
        }
    }
    return $null
}

# validate a saved solution against the current field
function Test-Solution($json, [string[]]$moves) {
    $s = New-SimState $json
    foreach ($m in $moves) {
        $s2 = Invoke-SimStep $s $m
        if ($null -eq $s2) { return $false }
        $s = $s2
    }
    return (Test-SimWin $s)
}

# ---------- load saved solutions ----------
$savedLevels = @{}
if (Test-Path $WtPath) {
    try {
        $wt = Get-Content $WtPath -Raw -Encoding UTF8 | ConvertFrom-Json
        foreach ($p in $wt.levels.PSObject.Properties) {
            $savedLevels[$p.Name] = $p.Value
        }
    }
    catch { Log "WARN: failed to parse walkthrough.json: $($_.Exception.Message)" }
}

# ---------- main loop ----------
try { $s = Get-State }
catch {
    Log "ERROR: dev-agent is not reachable at $BaseUrl"
    Log "Start the game: java `"-Djava.library.path=desktop\target\natives`" -jar desktop\target\yy-desktop-1.0-SNAPSHOT-jar-with-dependencies.jar agent"
    exit 1
}
Log "agent reachable: screen=$($s.screen), level=$($s.level), steps=$($s.steps)"

$newLevels = @{}
foreach ($k in $savedLevels.Keys) { $newLevels[$k] = $savedLevels[$k] }

$okCount = 0; $failCount = 0
foreach ($lvl in $levelList) {
    Log "--- level $lvl ---"

    Send-Start $lvl
    Start-Sleep -Milliseconds 700
    $st = $null
    $s = $null
    $deadline = (Get-Date).AddSeconds(30)
    while ((Get-Date) -lt $deadline) {
        $s = Get-State
        if ($s.screen -eq 'game2' -and [int]$s.level -eq $lvl -and [int]$s.steps -eq 0 -and -not $s.won -and -not $s.busy) { $st = $s; break }
        Start-Sleep -Milliseconds 200
    }
    if ($null -eq $st) {
        Log "ERROR: level $lvl did not start (screen=$($s.screen), level=$($s.level))"
        $failCount++
        break
    }
    Start-Sleep -Milliseconds 400

    # 1) try the saved solution first
    $moves = $null
    if (-not $ForceSolve -and $savedLevels.ContainsKey([string]$lvl)) {
        $mv = @($savedLevels[[string]$lvl].moves)
        if (Test-Solution $st $mv) {
            $moves = $mv
            Log "using saved solution ($($moves.Count) moves)"
        }
        else {
            Log "saved solution for level $lvl does not match the field, recomputing..."
        }
    }

    # 2) solve with BFS
    if ($null -eq $moves) {
        $sw = [Diagnostics.Stopwatch]::StartNew()
        $moves = Find-Solution $st $MaxStates
        if ($null -eq $moves) {
            Log "ERROR: no solution found (level $lvl, MaxStates=$MaxStates)"
            $failCount++
            break
        }
        Log ("solved by BFS in {0:n1} s, moves: {1}" -f $sw.Elapsed.TotalSeconds, $moves.Count)
    }

    # 3) execute moves
    foreach ($m in $moves) {
        Send-Move $m
        [void](Wait-NotBusy 30)
        if ($MoveDelayMs -gt 0) { Start-Sleep -Milliseconds $MoveDelayMs }
    }

    # 4) wait for the win window
    $st2 = Wait-Won 90
    if ($st2 -and $st2.won) {
        Log ("level {0} DONE in {1} moves: {2}" -f $lvl, $st2.steps, ($moves -join ','))
        # let the win screen play for a while before moving on
        if ($LevelDelaySec -gt 0) {
            Log ("waiting {0} s on the win screen..." -f $LevelDelaySec)
            Start-Sleep -Seconds $LevelDelaySec
        }
        $okCount++
        if (-not $NoSave) {
            $newLevels[[string]$lvl] = @{
                moves   = @($moves)
                steps   = [int]$st2.steps
                updated = (Get-Date -Format 'yyyy-MM-dd')
            }
        }
    }
    else {
        Log "ERROR: level $lvl not completed (steps=$($st2.steps), won=$($st2.won))"
        $failCount++
        break
    }
}

if (-not $NoSave) {
    @{ levels = $newLevels } | ConvertTo-Json -Depth 6 | Set-Content -Path $WtPath -Encoding UTF8
    Log "walkthrough.json updated"
}

Log ("SUMMARY: solved {0}, failed {1} (levels {2})" -f $okCount, $failCount, ($levelList -join ','))
if ($failCount -gt 0) { exit 2 }
exit 0
