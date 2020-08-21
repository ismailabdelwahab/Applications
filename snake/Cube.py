#!/usr/bin/env python3
import pygame

class Cube():
    rows = 25
    columns = 25
    width = 500
    height = 500
    def __init__(self, pos, xDir=1, yDir=0, color=(255,0,0)):
        self.pos = pos
        self.xDir = 1
        self.yDir = 0
        self.color = color

    def move(self, xDir, yDir):
        self.xDir = xDir
        self.yDir = yDir
        self.pos = (self.pos[0]+self.xDir, self.pos[1]+self.yDir)

    def draw(self, win, eyes=False):
        dis = self.width // self.rows
        i = self.pos[0]
        j = self.pos[1]
        pygame.draw.rect(win, self.color,(i*dis+1,j*dis+1,dis-2,dis-2))
        if eyes:
            center = dis//2
            radius = 3
            circleMiddle = (i*dis+center-radius,j*dis+8)
            circleMiddle2 = (i*dis + dis-radius*2, j*dis+8)
            pygame.draw.circle(win, (0,0,0), circleMiddle, radius)
            pygame.draw.circle(win, (0,0,0), circleMiddle2, radius)

