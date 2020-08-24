from Model.Swapper import swap_elements

def cocktail_shaker_sort(app, array):
	swapped = True
	while swapped:
		swapped = False
		for i in  range(len(array)-1):
			if array[i] > array[i+1]:
				swapped = True
				swap_elements(app, array, i, i+1)
		if not swapped: return # if no swapp was done then the array is sorted.
		for i in range(len(array)-1, 0, -1):
			if array[i] < array[i-1]:
				swap_elements(app, array, i, i-1)
				swapped = True
