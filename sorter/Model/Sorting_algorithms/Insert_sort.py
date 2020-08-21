from Model.Swapper import swap_elements

def insert_sort(app, array):
	index_to_insert = 0
	while index_to_insert < len(array):
		insert_element(app, array, index_to_insert)
		index_to_insert += 1

def insert_element(app, array, index_to_insert):
	i_first_element_superior = findFirstSuperior(array, index_to_insert)
	if i_first_element_superior != index_to_insert:
		swap_elements(app, array, index_to_insert, i_first_element_superior)
		shiftSortedElements(app,array,i_first_element_superior, index_to_insert)

def shiftSortedElements(app, array, i_first_element_superior, index_to_insert):
	while(i_first_element_superior < index_to_insert):
		i_first_element_superior += 1
		swap_elements( app, array, i_first_element_superior, index_to_insert)

def findFirstSuperior(array, index_to_insert):
	element = array[index_to_insert]
	for i in range( index_to_insert+1 ):
		if array[i] > element :
			return i
	return index_to_insert