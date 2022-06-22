package tphistory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;

public class FixedSizeQueue<K> extends ArrayDeque<K> {

	private final int maxSize;

	public FixedSizeQueue(int size){
		this.maxSize = size;
	}

	public void push(@NotNull K k){
		super.push(k);
		if (this.size() > maxSize){
			this.removeLast();
		}
	}
}
