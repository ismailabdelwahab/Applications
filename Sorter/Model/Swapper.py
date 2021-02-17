from time import sleep

def swap_elements(app, array, i=-1, j=-1):
	waiting_time_in_ms = 2
	array[i], array[j] = array[j], array[i]
	app.after( waiting_time_in_ms, lambda : app.redraw(j, i))
	sleep( waiting_time_in_ms/100 )
