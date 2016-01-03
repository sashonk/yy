package com.me.test;


public class Pair<T1 , T2> {
	public Pair(T1 page, T2 labels){
		_first = page;
		_second = labels;
	}
	
	public T1 getFirst() {
		return _first;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_second == null) ? 0 : _second.hashCode());
		result = prime * result + ((_first == null) ? 0 : _first.hashCode());
		return result;
	}
	
	public void setSecond(T2 val){
		_second = val;
	}
	
	public void setFirst(T1 f){
		_first = f;
	}

/*	public T1 get_first() {
		return _first;
	}

	public void set_first(T1 _first) {
		this._first = _first;
	}

	public T2 get_second() {
		return _second;
	}

	public void set_second(T2 _second) {
		this._second = _second;
	}*/

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (_second == null) {
			if (other._second != null)
				return false;
		} else if (!_second.equals(other._second))
			return false;
		if (_first == null) {
			if (other._first != null)
				return false;
		} else if (!_first.equals(other._first))
			return false;
		return true;
	}

	public T2 getSecond() {
		return _second;
	}
	
/*	public void setFirst(T1 t1) {
		 _pair = t1;
	}

	public void setSecond(T2 t2) {
		_labels = t2;
	}*/


	T1 _first;
	T2 _second;
}
