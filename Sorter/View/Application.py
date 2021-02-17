try:
	import tkinter as tk
except ImportError:
	import Tkinter as tk
import threading
from random import shuffle

from Model.Sorting_algorithms.Bubble_sort import bubble_sort
from Model.Sorting_algorithms.Cocktail_shaker_sort import cocktail_shaker_sort
from Model.Sorting_algorithms.Insert_sort import insert_sort
from Model.Sorting_algorithms.Selection_sort import selection_sort
from Model.Sorting_algorithms.Gnome_sort import gnome_sort

from Model.Sorting_algorithms.Quick_sort import quick_sort
from Model.Sorting_algorithms.Merge_sort import merge_sort


class Application(tk.Frame):
	WINDOW_DIMENSION = "1040x420"
	CANVAS_WIDTH = 600
	CANVAS_HEIGHT = 400
	CANVAS_BG = "black"
	def __init__(self, array=None):
		self.array = array
		self.create_master_window()
		self.create_canvas(); self.fill_canvas()
		self.create_buttons()

	def redraw(self, i=-1, j=-1):
		self.clear_canvas(); self.fill_canvas(i, j)
		
	def fill_canvas(self, i=-1, j=-1):
		X_PADDING = 3 ; Y_PADDING = 5
		c_width = int(self.canvas['width']) ; c_heigth = int(self.canvas['height']) - Y_PADDING
		block_width = c_width / len(self.array) ; block_heigth = c_heigth / len(self.array)

		for index, num in enumerate(self.array):
			color = 'white' if index != i else 'red'
			color = color if index != j else 'green'
			x1 = X_PADDING + index * block_width      ; y1 = c_heigth - Y_PADDING - num*block_heigth
			x2 = X_PADDING + (index+1) * block_width  ; y2 = c_heigth - Y_PADDING
			self.canvas.create_rectangle(x1,y1, x2,y2, fill=color)

	def clear_canvas(self):
		self.canvas.delete('all')

	def shuffle_array(self):
		shuffle(self.array)
		self.redraw()

	def update_array_len(self, n):
		self.array = [x for x in range(n)]
		print("Array length updated to [ {} ].".format(str(n)))
		self.redraw()

############# Creating stuff ######################################################################################
	def create_master_window(self):
		self.master = tk.Tk()
		self.master.geometry( self.WINDOW_DIMENSION )
		self.master.configure(bg="gray76")
		super().__init__(self.master)
		self.grid()

	def create_canvas(self):
		self.canvas = tk.Canvas(self.master, bg=self.CANVAS_BG, width=self.CANVAS_WIDTH, height=self.CANVAS_HEIGHT)
		self.canvas.grid(row=0, column=1)

	def create_buttons(self):
		left_pannel = tk.Frame(self)
		left_pannel.grid(row=0,column=0, sticky="n")
		####################################################### ROW = 0
		nb_elem_label = tk.Label(left_pannel, text="Number of elements:")
		nb_elem_label.grid(row=0, column=0)

		nb = tk.StringVar()
		nb.set(str( len(self.array) ))
		self.nb_elem_entry = tk.Entry(left_pannel, textvariable=nb, justify="center", width=6)
		self.nb_elem_entry.grid(row=0, column=1)
		
		nb_elem_button = tk.Button(left_pannel, text="Edit changes", fg="black", 
			command=lambda: self.update_array_len( int(self.nb_elem_entry.get()) ))
		nb_elem_button.grid(row=0, column=2)

		####################################################### ROW = 1
		fill_canvas_button = tk.Button(left_pannel, text="Show bars", fg="green",command=self.fill_canvas)
		fill_canvas_button.grid(row=1, column=0)
		clear_canvas_button = tk.Button(left_pannel, text="Hide bars", fg="red",command=self.clear_canvas)
		clear_canvas_button.grid(row=1, column=1)

		suffle_array_button = tk.Button(left_pannel, text="Shuffle the array", fg="black",command=self.shuffle_array)
		suffle_array_button.grid(row=1, column=2)
		####################################################### ROW = 2

		####################################################### ROW = 3 - 5 ### O( n² ) Algos:
		n_squared = tk.Label( left_pannel, text="O(n²) algorithms:" )
		n_squared.grid(row=3, column=0)
		bubble_sort_button= tk.Button(left_pannel, text="Bubble sort", fg="purple",
			command= lambda : threading.Thread(target=bubble_sort, args=[self, self.array]).start())
		bubble_sort_button.grid(row=4, column=0)
		cocktail_shaker_sort_button= tk.Button(left_pannel, text="Cocktail shaker", fg="purple",
			command= lambda : threading.Thread(target=cocktail_shaker_sort, args=[self, self.array]).start())
		cocktail_shaker_sort_button.grid(row=4, column=1)
		insert_sort_button= tk.Button(left_pannel, text="Insert sort", fg="purple",
			command= lambda : threading.Thread(target=insert_sort, args=[self, self.array]).start())
		insert_sort_button.grid(row=5, column=0)
		selection_sort_button= tk.Button(left_pannel, text="Selection sort", fg="purple",
			command= lambda : threading.Thread(target=selection_sort, args=[self, self.array]).start())
		selection_sort_button.grid(row=5, column=1)
		gnome_sort_button= tk.Button(left_pannel, text="Gnome sort", fg="purple",
			command= lambda : threading.Thread(target=gnome_sort, args=[self, self.array]).start())
		gnome_sort_button.grid(row=5, column=2)

		####################################################### ROW = 6 - 8 ### O( n*log(n) ) Algos:
		n_log_n = tk.Label( left_pannel, text="O(n*log(n)) algorithms:" )
		n_log_n.grid(row=6, column=0)
		quick_sort_button= tk.Button(left_pannel, text="Quick sort", fg="purple",
			command= lambda : threading.Thread(target=quick_sort, args=[self, self.array,0, len(self.array)-1]).start())
		quick_sort_button.grid(row=7, column=0)
		merge_sort_button= tk.Button(left_pannel, text="Merge sort", fg="purple",
			command= lambda : threading.Thread(target=merge_sort, args=[self, self.array,0, len(self.array)-1]).start())
		merge_sort_button.grid(row=7, column=1)

		####################################################### ROW = 10
		self.quit = tk.Button(left_pannel, text="QUIT", fg="red",command=self.master.destroy)
		self.quit.grid(row=10, column=1)