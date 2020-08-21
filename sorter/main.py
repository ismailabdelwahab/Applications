#!/usr/bin/env python3
from View.Application import Application

if __name__ == '__main__':
	NB_OF_ELEMENTS = 200
	array = [x for x in range(0, NB_OF_ELEMENTS)]
	app = Application( array=array )

	app.mainloop()