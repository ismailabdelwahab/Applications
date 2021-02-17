from Model.Swapper import swap_elements

def quick_sort(app, array, low, high):
	print("Starting {Quick sort} ...")
	quick_sort_rec(app, array, low, high)
	print("End of {Quick sort}.")

def quick_sort_rec(app, array, low, high):
	if low < high :
		indexPivot = partitionArray(app, array, low, high)
		quick_sort_rec(app, array, low, indexPivot)
		quick_sort_rec(app, array, indexPivot+1, high)
		
def partitionArray(app, array, low, high):
		pivot = array[low]
		indexOfLowers = low
		swap_elements(app, array, low, high)
		for i in range(low, high):
			if array[i] < pivot:
				swap_elements( app, array, i, indexOfLowers)
				indexOfLowers += 1

		swap_elements(app, array, high, indexOfLowers)
		return indexOfLowers