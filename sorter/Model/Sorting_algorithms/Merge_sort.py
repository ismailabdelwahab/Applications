from Model.Swapper import swap_elements
from time import sleep

def update_view(app, red_index=-1, green_index=-1):
	app.after( 2, lambda : app.redraw(red_index, green_index))
	sleep(2/100)

def merge_sort(app, array, from1, to1):
	print("Starting {Merge sort} ...")
	merge_sort_rec(app, array, from1, to1)
	print("End of {Merge sort}.")


def merge_sort_rec(app, array, from1, to1):
	if to1 == from1 : return

	last_index = to1
	middle_index = ( from1 + to1 ) // 2
	merge_sort_rec( app, array,      from1    , middle_index )
	merge_sort_rec( app, array, middle_index+1, last_index   )

	merge_arrays(app, array, from1, middle_index, middle_index+1, last_index)

def merge_arrays(app, array, from1, to1, from2, to2):
	#print("Merging from {} to {} ... ".format(from1, to2))
	index = from1
	arr1 = array[from1:to1+1]; arr2 = array[from2:to2+1]
	i_arr1 = 0; size_arr1 = to1 - from1 + 1
	i_arr2 = 0; size_arr2 = to2 - from2 + 1

	# Merge arr1 and arr2
	while i_arr1 < size_arr1 and i_arr2 < size_arr2:
		if arr1[i_arr1] < arr2[i_arr2]:
			array[index] = arr1[i_arr1]
			i_arr1 += 1
		else:
			array[index] = arr2[i_arr2]
			i_arr2 += 1
		index += 1
		update_view(app, index, from1)
	# Case where there is still elements in arr1 after the merge:
	while i_arr1 < size_arr1:
		array[index] = arr1[i_arr1]
		i_arr1 += 1; index += 1
		update_view(app, index, from1)
	# Case for arr2:
	while i_arr2 < size_arr2 :
		array[index] = arr2[i_arr2]
		i_arr2 += 1; index += 1
		update_view(app, index, from1)