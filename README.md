# cw-temple
Coursework 4 

Coursework 4 focused on a map problem. George in a temple of doom, he had to run to get the orb, and then return to collecting as much gold as possible, noting that the map changed when the orb was collected.

A limitation with my method was that I couldn't make it efficient enough and I didn't research graph search algorthyms such as A* and another, which colleagues told me about after their submissions. I looked at this problem from scratch spending many hours thinking of solutions. The solution I came up with for escape would have found the most optimal route much better if I had managed to make it more efficient, however, I quickly realised on a large map, the processing power required to analyse every single possible route on a map was too much for the 10 second limit. Eventually I came to the conclusion that I would cut off dead ends to avoid wasting time, although this did restrict the possible paths that led to the exit a little too harshly and in some cases, reducing the paths found to 0.

I ended up implementing two methods, one which prioritised dealing with paths that had taken the longest first (meaning the list would reduce dramatically quickly, because we would start to cut off lists that are longer that meet dead ends and those that go over the time limit), and I also implemented a back up method that dealt with the shorter methods first, but this method would find less gold and a more direct route to the exit. The second method only being used if the first method failed to find an escape route. These two methods combined led to an 100% success rate at escaping the maze.


