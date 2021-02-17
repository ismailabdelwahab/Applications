from Model.Swapper import swap_elements

def selection_sort(app, array):
	print("Starting {Selection sort} ...")
	for i in range( len(array)-1 ):
		index_of_min = i
		for j in range(i,len(array)) :
			if array[j] < array[index_of_min]:
				index_of_min = j
		if index_of_min != i:
			swap_elements(app, array, index_of_min, i)
	print("End of {Selection sort}.")