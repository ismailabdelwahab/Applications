from Model.Swapper import swap_elements

def is_sorted(array):
	for i in range( len(array)-1 ):
		if array[i] > array[i+1]: return False
	return True

def bubble_sort(app, array):
	print("Starting {Bubble sort} ...")
	while not is_sorted(array):
		for i in range( len(array)-1 ):
			if array[i] > array[i+1]:
				swap_elements( app, array, i, i+1 )
	print("End of {Bubble sort}.")
