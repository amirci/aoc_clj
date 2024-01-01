import itertools
from collections import defaultdict

path = defaultdict(list)


def grid_to_heats(grid):
  return [[int(x) for x in row] for row in grid]

def parse_input(file):
    with open(file) as file_desc:
        raw_file = file_desc.read()

    raw_file = raw_file.strip()
    grid_rows = raw_file.split("\n")


    return grid_to_heats(grid_rows)


def ultra_neighbours(x, y, dx, dy, steps):
    found = []

    def append(t):
        found.append(t)

    if steps <= 9:
        append((x + dx, y + dy, dx, dy, steps + 1))

    if steps >= 4:
        append((x - dy, y + dx, -dy, dx, 1))
        append((x + dy, y - dx, dy, -dx, 1))

    return found


def find_neighbours(x, y, dx, dy, steps):
    found = []

    def append(t):
        found.append(t)

    if steps <= 2:
        append((x + dx, y + dy, dx, dy, steps + 1))

    append((x - dy, y + dx, -dy, dx, 1))
    append((x + dy, y - dx, dy, -dx, 1))

    return found


def target_is_equal(target, row, col, _):
    return (row, col) == target

def target_found_ultra(target, row, col, steps):
    return target_is_equal(target, row, col, 0) and steps >= 4

def path_heath_min_loss(
        heat_map,
        neighbours=find_neighbours,
        target_found=target_is_equal
):
    pending = {}
    visited = {}

    pending[0]= {(0, 0, 0, 1, 0), (0, 0, 1, 0, 0)}


    height = len(heat_map)
    width = len(heat_map[0])

    target = (height - 1, width - 1)

    found = None

    print("\nThe target", target)

    def in_range(node):
        row, col, *_ = node
        return 0 <= col < width and 0 <= row < height

    idx = 0

    while pending and not found and idx < 100000:
        idx+=1
        min_heat = min(pending.keys())
        nodes = pending.pop(min_heat)


        for state in nodes:

            row, col, dx, dy, steps = state

            found = (min_heat, state) if target_found(target, row, col, steps) else None

            if found:
                break;

            nodes = neighbours(row, col, dx, dy, steps)

            nodes = filter(in_range, nodes)

            for n in nodes:
                history = path[(row, col, dx, dy, steps)]

                if n not in visited:
                    nr, nc, *_ = n
                    path[n] = history + [n]
                    new_heat = min_heat + heat_map[nr][nc]
                    pending.setdefault(new_heat, set()).add(n)
                    visited[n] = new_heat

    return found, idx


sample = ["2413432311323",
          "3215453535623",
          "3255245654254",
          "3446585845452",
          "4546657867536",
          "1438598798454",
          "4457876987766",
          "3637877979653",
          "4654967986887",
          "4564679986453",
          "1224686865563",
          "2546548887735",
          "4322674655533"]

sample_heats = grid_to_heats(sample)
input_heats = grid_to_heats(parse_input("../../../resources/2023/day17.txt"))

print(path_heath_min_loss(sample_heats))
print(path_heath_min_loss(input_heats))

sample_ultra = ["111111111111",
                "999999999991",
                "999999999991",
                "999999999991",
                "999999999991"]

sample_ultra_heats = grid_to_heats(sample_ultra)

print(path_heath_min_loss(sample_heats, ultra_neighbours, target_found_ultra))
print(path_heath_min_loss(sample_ultra_heats, ultra_neighbours, target_found_ultra))
print(path_heath_min_loss(input_heats, ultra_neighbours, target_found_ultra))
