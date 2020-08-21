#!/usr/bin/env python3
import pygame
import random

from Snake import Snake
from Cube import Cube

def drawGrid(win, width, height, columns, rows):
    WHITE = (255,255,255)
    xBoxSize = width // columns
    yBoxSize = height // rows
    x = y = 0
    for _ in range(rows):
        x += xBoxSize
        y += yBoxSize
        pygame.draw.line(win, WHITE, (x,0), (x,width) )
        pygame.draw.line(win, WHITE, (0,y), (width,y) )

def redrawWindow(win, width, height, columns, rows, snake, food):
    BLACK = (0,0,0)
    win.fill( BLACK )
    drawGrid(win, width, height ,columns, rows)
    snake.draw( win )
    food.draw( win )
    pygame.display.update()

def randomFood(rows, item):
    pos = item.body
    while True:
        x = random.randrange(rows)
        y = random.randrange(rows)
        if len(list(filter(lambda z: z.pos == (x,y), pos )))>0:
            continue
        else:
            break
    return (x,y)
if __name__ == '__main__':
    WIDTH = 500
    HEIGHT = 500
    rows = 25
    columns = 25
    RED = (255,0,0)
    win = pygame.display.set_mode((WIDTH, HEIGHT))
    
    starting_position = (columns//2, rows//2)
    snake = Snake( starting_position, RED )
    food = Cube( randomFood(rows, snake), color=(0,255,0) )

    clock = pygame.time.Clock()

    run = True
    while run :
        pygame.time.delay(50)
        clock.tick(10)
        snake.move()
        if snake.body[0].pos == food.pos:
            snake.addCube()
            food = Cube( randomFood(rows, snake), color=(0,255,0) )
        for x in range(len(snake.body)):
            if snake.body[x].pos in list(map(lambda z: z.pos, snake.body[x+1:])):
                print("Score is: ", len(snake.body))
                snake.respawn()
                break
        
        redrawWindow(win, WIDTH, HEIGHT, columns, rows, snake, food)
