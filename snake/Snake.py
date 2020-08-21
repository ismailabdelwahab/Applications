#!/usr/bin/env python3
import pygame

from Cube import Cube

class Snake():
    def __init__(self, starting_position, color):
        self.starting_position = starting_position
        self.head = Cube( starting_position )
        self.body = [self.head]
        self.color = color
        self.xDir = 1
        self.yDir = 0
        self.turns = {}

    def addCube(self):
        tail = self.body[-1]
        dx, dy = tail.xDir, tail.yDir

        if dx == 1 and dy == 0 :
            self.body.append( Cube((tail.pos[0]-1, tail.pos[1])) )
        if dx == -1 and dy == 0 :
            self.body.append( Cube((tail.pos[0]+1, tail.pos[1])) )
        if dx == 0 and dy == 1 :
            self.body.append( Cube((tail.pos[0], tail.pos[1]-1)) )
        if dx == 1 and dy == -1 :
            self.body.append( Cube((tail.pos[0], tail.pos[1]+1)) )
        self.body[-1].xDir = dx
        self.body[-1].yDir = dy
    
    def respawn(self):
        self.head = Cube( self.starting_position )
        self.body = [self.head]
        self.turns= {}
        self.xDir = 1
        self.yDir = 0

    def move(self):
        for event in pygame.event.get():
            if event.type == pygame.QUIT :
                pygame.quit()
            keys = pygame.key.get_pressed()
            for key in keys:
                if keys[pygame.K_LEFT]:
                    self.xDir = -1
                    self.yDir = 0
                    self.turns[self.head.pos] = [self.xDir, self.yDir]
                if keys[pygame.K_RIGHT]:
                    self.xDir = 1
                    self.yDir = 0
                    self.turns[self.head.pos] = [self.xDir, self.yDir]
                if keys[pygame.K_UP]:
                    self.xDir = 0
                    self.yDir = -1
                    self.turns[self.head.pos] = [self.xDir, self.yDir]
                if keys[pygame.K_DOWN]:
                    self.xDir = 0
                    self.yDir = 1
                    self.turns[self.head.pos] = [self.xDir, self.yDir]

        for i, c in enumerate(self.body):
            p = c.pos[:]
            if p in self.turns:
                turn = self.turns[p]
                c.move( turn[0], turn[1] )
                if i == len(self.body)-1:
                    self.turns.pop(p)
            else:
                if c.xDir == -1 and c.pos[0] <= 0: c.pos = (c.columns-1,c.pos[1])
                elif c.xDir == 1 and c.pos[0] >= c.columns-1: c.pos = (0,c.pos[1])
                elif c.yDir == -1 and c.pos[1] <= 0: c.pos = (c.pos[0],c.rows-1)
                elif c.yDir == 1 and c.pos[1] >= c.rows: c.pos = (c.pos[0],0)
                else: 
                    c.move(c.xDir, c.yDir)
    def draw(self, win):
        for i, c in enumerate(self.body):
            if i == 0 : c.draw(win, True)
            else: c.draw(win)
