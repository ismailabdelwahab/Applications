from Model.Swapper import swap_elements

def gnome_sort(app, array):
	print("Starting {Gnome sort} ...")
	position = 0
	while position < len(array)-1:
		if (position == -1 or array[position] <= array[position+1]):
			position += 1
		else:
			swap_elements(app, array, position, position+1)
			position -= 1
	print("End of {Gnome sort}.")