package it.diunipi.volpi.sycamore.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This class represents a list that is always kept sorted, but that offers random access. The list
 * can contains just Comparable objects and it allows the presence of duplicate objects.
 * 
 * @author Vale
 * 
 * @param <E>
 */
public class SortedList<E extends Comparable<E>> implements List<E>, Serializable, Collection<E>
{
	private static final long	serialVersionUID	= 7754342725919180590L;

	private ArrayList<E>		internalList		= null;
	private Comparator<E>		comparator			= null;

	/**
	 * Default constructor. Creates a list sorted using natural ordering.
	 */
	public SortedList()
	{
		this.internalList = new ArrayList<E>();
	}

	/**
	 * Default constructor. Creates a list sorted using passed Comparator.
	 */
	public SortedList(Comparator<E> comparator)
	{
		this();
		this.comparator = comparator;
	}

	/**
	 * Finds the index where passed object should be inserted.
	 * 
	 * @param e
	 * @return
	 */
	private int findIndex(E e)
	{
		if (this.comparator != null)
		{
			int n = Collections.binarySearch(this.internalList, e, this.comparator);
			return n;
		}
		else
		{
			int n = Collections.binarySearch(this.internalList, e);
			return n;
		}
	}

	/* (non-Javadoc)
	 * @see java.util.List#add(java.lang.Object)
	 */
	@Override
	public boolean add(E e)
	{
		int find = this.findIndex(e);
		int index = find < 0 ? (-find - 1) : find;

		internalList.add(index, e);
		return false;
	}

	/**
	 * DO NOT USE.
	 * Throws an UnsupportedOperationException because it is not allowed to ask for a specific index to the sorted list.
	 */
	@Override
	public void add(int index, E e)
	{
		throw new UnsupportedOperationException(
				"Impossible to pre-decide indexes in a sorted list! Use add(E e) instead.");
	}

	/* (non-Javadoc)
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> e)
	{
		Iterator<? extends E> iterator = e.iterator();
		boolean b = true;

		while (iterator.hasNext())
		{
			b = b && this.add(iterator.next());
		}
		return b;
	}

	/**
	 * DO NOT USE.
	 * Throws an UnsupportedOperationException because it is not allowed to ask for a specific index to the sorted list.
	 */
	@Override
	public boolean addAll(int arg0, Collection<? extends E> arg1)
	{
		throw new UnsupportedOperationException(
				"Impossible to pre-decide indexes in a sorted list! Use addAll(arg1) instead.");
	}

	/* (non-Javadoc)
	 * @see java.util.List#clear()
	 */
	@Override
	public void clear()
	{
		this.internalList.clear();
	}

	/* (non-Javadoc)
	 * @see java.util.List#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o)
	{
		return this.internalList.contains(o);
	}

	/* (non-Javadoc)
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c)
	{
		return this.internalList.containsAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.List#get(int)
	 */
	@Override
	public E get(int index)
	{
		return this.internalList.get(index);
	}

	/* (non-Javadoc)
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(Object o)
	{
		return this.internalList.indexOf(o);
	}

	/**
	 * Returns the first element of this list
	 */
	public E firstElement()
	{
		return this.internalList.get(0);
	}

	/**
	 * Returns the last element of this list
	 */
	public E lastElement()
	{
		return this.internalList.get(this.internalList.size() - 1);
	}

	/* (non-Javadoc)
	 * @see java.util.List#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return this.internalList.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.List#iterator()
	 */
	@Override
	public Iterator<E> iterator()
	{
		return this.internalList.iterator();
	}

	/* (non-Javadoc)
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	@Override
	public int lastIndexOf(Object o)
	{
		return this.internalList.lastIndexOf(o);
	}

	/* (non-Javadoc)
	 * @see java.util.List#listIterator()
	 */
	@Override
	public ListIterator<E> listIterator()
	{
		return this.internalList.listIterator();
	}

	/* (non-Javadoc)
	 * @see java.util.List#listIterator(int)
	 */
	@Override
	public ListIterator<E> listIterator(int index)
	{
		return this.listIterator(index);
	}

	/* (non-Javadoc)
	 * @see java.util.List#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o)
	{
		return this.internalList.remove(o);
	}

	/* (non-Javadoc)
	 * @see java.util.List#remove(int)
	 */
	@Override
	public E remove(int index)
	{
		return this.internalList.remove(index);
	}

	/* (non-Javadoc)
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c)
	{
		return this.internalList.removeAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c)
	{
		return this.internalList.retainAll(c);
	}

	/**
	 * DO NOT USE.
	 * Throws an UnsupportedOperationException because it is not allowed to ask for a specific index to the sorted list.
	 */
	@Override
	public E set(int arg0, E arg1)
	{
		throw new UnsupportedOperationException(
				"Impossible to pre-decide indexes in a sorted list!");
	}

	/* (non-Javadoc)
	 * @see java.util.List#size()
	 */
	@Override
	public int size()
	{
		return this.internalList.size();
	}

	/**
	 * DO NOT USE.
	 * Throws an UnsupportedOperationException because the backed list returned by this mehod could break the sorting in original list. A safer 
	 * implementation of this method is the subListClone() method, that returns a sublist whose elements are duplicates of the original elements, 
	 * and that is not backed by this list in a way that any modification to the returned list does not reflect on the original one.
	 */
	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		throw new UnsupportedOperationException("Impossible to "
				+ "return a backed list. Use subListClone(fromIndex, toIndex) instead.");
	}

	/**
	 * Returns a view of the portion of this list between the specified fromIndex, inclusive, and
	 * toIndex, exclusive. (If fromIndex and toIndex are equal, the returned list is empty.) The
	 * returned list is not backed by this list, so non-structural changes in the returned list are
	 * not reflected in this list.
	 */
	public Object subListClone(int fromIndex, int toIndex)
	{
		ArrayList<E> sublist = (ArrayList<E>) this.internalList.subList(fromIndex, toIndex);
		return sublist.clone();
	}

	/* (non-Javadoc)
	 * @see java.util.List#toArray()
	 */
	@Override
	public Object[] toArray()
	{
		return this.internalList.toArray();
	}

	/* (non-Javadoc)
	 * @see java.util.List#toArray(T[])
	 */
	@Override
	public <T> T[] toArray(T[] a)
	{
		return this.internalList.toArray(a);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return internalList.toString();
	}

	/**
	 * 
	 */
	public void removeAllElements()
	{
		for (int i = this.internalList.size() -1; i >= 0; i--)
		{
			this.internalList.remove(i);
		}
	}

}
