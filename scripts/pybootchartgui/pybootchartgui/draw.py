import cairo
import math
import re

# Process tree background color.
BACK_COLOR = (1.0, 1.0, 1.0, 1.0)

WHITE = (1.0, 1.0, 1.0, 1.0)
# Process tree border color.
BORDER_COLOR = (0.63, 0.63, 0.63, 1.0)
# Second tick line color.
TICK_COLOR = (0.92, 0.92, 0.92, 1.0)
# 5-second tick line color.
TICK_COLOR_BOLD = (0.86, 0.86, 0.86, 1.0)
# Text color.
TEXT_COLOR = (0.0, 0.0, 0.0, 1.0)

# Font family
FONT_NAME = "Bitstream Vera Sans"	
# Title text font.
TITLE_FONT_SIZE = 18
# Default text font.
TEXT_FONT_SIZE = 12
# Axis label font.
AXIS_FONT_SIZE = 11
# Legend font.
LEGEND_FONT_SIZE = 12
	
# CPU load chart color.
CPU_COLOR = (0.40, 0.55, 0.70, 1.0)
# IO wait chart color.
IO_COLOR = (0.76, 0.48, 0.48, 0.5)
# Disk throughput color.
DISK_TPUT_COLOR = (0.20, 0.71, 0.20, 1.0)
# CPU load chart color.
FILE_OPEN_COLOR = (0.20, 0.71, 0.71, 1.0)
	
# Process border color.
PROC_BORDER_COLOR = (0.71, 0.71, 0.71, 1.0)

PROC_COLOR_D = (0.76, 0.48, 0.48, 0.125)
# Running process color.
PROC_COLOR_R = CPU_COLOR
# Sleeping process color.
PROC_COLOR_S = (0.94, 0.94, 0.94, 1.0)
# Stopped process color.
PROC_COLOR_T = (0.94, 0.50, 0.50, 1.0)
# Zombie process color.
PROC_COLOR_Z = (0.71, 0.71, 0.71, 1.0)
# Dead process color.
PROC_COLOR_X = (0.71, 0.71, 0.71, 0.125)
# Paging process color.
PROC_COLOR_W = (0.71, 0.71, 0.71, 0.125)

# Process label color.
PROC_TEXT_COLOR = (0.19, 0.19, 0.19, 1.0)
# Process label font.
PROC_TEXT_FONT_SIZE = 12

# Signature color.
SIG_COLOR = (0.0, 0.0, 0.0, 0.3125)
# Signature font.
SIG_FONT_SIZE = 14
# Signature text.
SIGNATURE = "http://code.google.com/p/pybootchartgui"
	
# Process dependency line color.
DEP_COLOR = (0.75, 0.75, 0.75, 1.0)
# Process dependency line stroke.
DEP_STROKE = 1.0

# Process description date format.
DESC_TIME_FORMAT = "mm:ss.SSS"

# Configure task color
TASK_COLOR_CONFIGURE = (1.0, 1.0, 0.00, 1.0)
# Compile task color.
TASK_COLOR_COMPILE = (0.0, 1.00, 0.00, 1.0)
# Install task color
TASK_COLOR_INSTALL = (1.0, 0.00, 1.00, 1.0)
# Package task color
TASK_COLOR_PACKAGE = (0.0, 1.00, 1.00, 1.0)
# Sysroot task color
TASK_COLOR_SYSROOT = (0.0, 0.00, 1.00, 1.0)

# Process states
STATE_UNDEFINED = 0
STATE_RUNNING   = 1
STATE_SLEEPING  = 2
STATE_WAITING   = 3
STATE_STOPPED   = 4
STATE_ZOMBIE    = 5

STATE_COLORS = [(0,0,0,0), PROC_COLOR_R, PROC_COLOR_S, PROC_COLOR_D, PROC_COLOR_T, PROC_COLOR_Z, PROC_COLOR_X, PROC_COLOR_W]

# Convert ps process state to an int
def get_proc_state(flag):
	return "RSDTZXW".index(flag) + 1
	

def draw_text(ctx, text, color, x, y):
	ctx.set_source_rgba(*color)
	ctx.move_to(x, y)
	ctx.show_text(text)
	
	
def draw_fill_rect(ctx, color, rect):
	ctx.set_source_rgba(*color)
	ctx.rectangle(*rect)
	ctx.fill()
	

def draw_rect(ctx, color, rect):
	ctx.set_source_rgba(*color)
	ctx.rectangle(*rect)
	ctx.stroke()
	
	
def draw_legend_box(ctx, label, fill_color, x, y, s):
	draw_fill_rect(ctx, fill_color, (x, y - s, s, s))
	draw_rect(ctx, PROC_BORDER_COLOR, (x, y - s, s, s))
	draw_text(ctx, label, TEXT_COLOR, x + s + 5, y)
	
     
def draw_legend_line(ctx, label, fill_color, x, y, s):
	draw_fill_rect(ctx, fill_color, (x, y - s/2, s + 1, 3))    
	ctx.arc(x + (s + 1)/2.0, y - (s - 3)/2.0, 2.5, 0, 2.0 * math.pi)
	ctx.fill()
	draw_text(ctx, label, TEXT_COLOR, x + s + 5, y)
	

def draw_label_in_box(ctx, color, label, x, y, w, maxx):
	label_w = ctx.text_extents(label)[2]
	label_x = x + w / 2 - label_w / 2
        if label_w + 10 > w:
            label_x = x + w + 5
        if label_x + label_w > maxx:
            label_x = x - label_w - 5
	draw_text(ctx, label, color, label_x, y)


def draw_5sec_labels(ctx, rect, sec_w):
        ctx.set_font_size(AXIS_FONT_SIZE)
	for i in range(0, rect[2] + 1, sec_w):
		if ((i / sec_w) % 30 == 0) :
			label = "%ds" % (i / sec_w)
			label_w = ctx.text_extents(label)[2]
			draw_text(ctx, label, TEXT_COLOR, rect[0] + i - label_w/2, rect[1] - 2)


def draw_box_ticks(ctx, rect, sec_w):
	draw_rect(ctx, BORDER_COLOR, tuple(rect))
	
	ctx.set_line_cap(cairo.LINE_CAP_SQUARE)

	for i in range(sec_w, rect[2] + 1, sec_w):
		if ((i / sec_w) % 30 == 0) :
			ctx.set_source_rgba(*TICK_COLOR_BOLD)
		else :
			ctx.set_source_rgba(*TICK_COLOR)
		ctx.move_to(rect[0] + i, rect[1] + 1)
		ctx.line_to(rect[0] + i, rect[1] + rect[3] - 1)
		ctx.stroke()

	ctx.set_line_cap(cairo.LINE_CAP_BUTT)

def draw_chart(ctx, color, fill, chart_bounds, data, proc_tree):
	ctx.set_line_width(0.5)
	x_shift = proc_tree.start_time
	x_scale = proc_tree.duration
    
	def transform_point_coords(point, x_base, y_base, xscale, yscale, x_trans, y_trans):
		x = (point[0] - x_base) * xscale + x_trans
		y = (point[1] - y_base) * -yscale + y_trans + bar_h
		return x, y

	xscale = float(chart_bounds[2]) / max(x for (x,y) in data)
	yscale = float(chart_bounds[3]) / max(y for (x,y) in data)
    
	first = transform_point_coords(data[0], x_shift, 0, xscale, yscale, chart_bounds[0], chart_bounds[1])
	last = transform_point_coords(data[-1], x_shift, 0, xscale, yscale, chart_bounds[0], chart_bounds[1])
    
	ctx.set_source_rgba(*color)
	ctx.move_to(*first)
	for point in data:
		x, y = transform_point_coords(point, x_shift, 0, xscale, yscale, chart_bounds[0], chart_bounds[1])
		ctx.line_to(x, y)
	if fill:
		ctx.stroke_preserve()
		ctx.line_to(last[0], chart_bounds[1]+bar_h)
		ctx.line_to(first[0], chart_bounds[1]+bar_h)
		ctx.line_to(first[0], first[1])
		ctx.fill()
	else:
		ctx.stroke()
	ctx.set_line_width(1.0)

header_h = 280
bar_h = 55
# offsets
off_x, off_y = 10, 10
sec_w = 1 # the width of a second
proc_h = 16 # the height of a process
leg_s = 10
MIN_IMG_W = 800


def extents(res):
	start = min(res.start.keys())
	end = max(res.end.keys())

	w = ((end - start) * sec_w) + 2*off_x
	h = proc_h * len(res.processes) + header_h + 2*off_y

	return (w,h)

#
# Render the chart.
# 
def render(ctx, res):
	(w, h) = extents(res)

	ctx.set_line_width(1.0)
	ctx.select_font_face(FONT_NAME)
	draw_fill_rect(ctx, WHITE, (0, 0, max(w, MIN_IMG_W), h))
	w -= 2*off_x    
	# draw the title and headers
	#curr_y = draw_header(ctx, headers, off_x, proc_tree.duration)
        curr_y = 0
    
	# render bar legend
	ctx.set_font_size(LEGEND_FONT_SIZE)

        #print "w, h %s %s" % (w, h)

	#draw_legend_box(ctx, "CPU (user+sys)", CPU_COLOR, off_x, curr_y+20, leg_s)
	#draw_legend_box(ctx, "I/O (wait)", IO_COLOR, off_x + 120, curr_y+20, leg_s)

	# render I/O wait
        #chart_rect = (off_x, curr_y+30, w, bar_h)
	#draw_box_ticks(ctx, chart_rect, sec_w)
	#draw_chart(ctx, IO_COLOR, True, chart_rect, [(sample.time, sample.user + sample.sys + sample.io) for sample in cpu_stats], proc_tree) 
	# render CPU load
	#draw_chart(ctx, CPU_COLOR, True, chart_rect, [(sample.time, sample.user + sample.sys) for sample in cpu_stats], proc_tree)

	#curr_y = curr_y + 30 + bar_h

	# render second chart
	#draw_legend_line(ctx, "Disk throughput", DISK_TPUT_COLOR, off_x, curr_y+20, leg_s)
	#draw_legend_box(ctx, "Disk utilization", IO_COLOR, off_x + 120, curr_y+20, leg_s)

        # render I/O utilization
	#chart_rect = (off_x, curr_y+30, w, bar_h)
	#draw_box_ticks(ctx, chart_rect, sec_w)
	#draw_chart(ctx, IO_COLOR, True, chart_rect, [(sample.time, sample.util) for sample in disk_stats], proc_tree)
				
	# render disk throughput
	#max_sample = max(disk_stats, key=lambda s: s.tput)
	#draw_chart(ctx, DISK_TPUT_COLOR, False, chart_rect, [(sample.time, sample.tput) for sample in disk_stats], proc_tree)
	
	#pos_x = off_x + ((max_sample.time - proc_tree.start_time) * w / proc_tree.duration)
        pos_x = off_x

	shift_x, shift_y = -20, 20
	if (pos_x < off_x + 245):
		shift_x, shift_y = 5, 40
       				
	#label = "%dMB/s" % round((max_sample.tput) / 1024.0)
	#draw_text(ctx, label, DISK_TPUT_COLOR, pos_x + shift_x, curr_y + shift_y)



        chart_rect = [off_x, curr_y+60, w, h - 2 * off_y - (curr_y+60) + proc_h]

	draw_legend_box(ctx, "Configure", 	TASK_COLOR_CONFIGURE, off_x    , curr_y + 45, leg_s)
	draw_legend_box(ctx, "Compile", 	TASK_COLOR_COMPILE, off_x+120, curr_y + 45, leg_s)
	draw_legend_box(ctx, "Install", 	TASK_COLOR_INSTALL, off_x+240, curr_y + 45, leg_s)
	draw_legend_box(ctx, "Package", 	TASK_COLOR_PACKAGE, off_x+360, curr_y + 45, leg_s)
	draw_legend_box(ctx, "Populate Sysroot",	TASK_COLOR_SYSROOT, off_x+480, curr_y + 45, leg_s)
	
	ctx.set_font_size(PROC_TEXT_FONT_SIZE)
	
	draw_box_ticks(ctx, chart_rect, sec_w)
	draw_5sec_labels(ctx, chart_rect, sec_w)

	y = curr_y+60

        offset = min(res.start.keys())
        for s in sorted(res.start.keys()):
            for val in sorted(res.start[s]):
                task = val.split(":")[1]
                #print val
                #print res.processes[val][1]
                #print s
                x = (s - offset) * sec_w
                w = ((res.processes[val][1] - s) * sec_w)

                #print "proc at %s %s %s %s" % (x, y, w, proc_h)
                col = None
                if task == "do_compile":
                    col = TASK_COLOR_COMPILE
                elif task == "do_configure":
                    col = TASK_COLOR_CONFIGURE
                elif task == "do_install":
                    col = TASK_COLOR_INSTALL
                elif task == "do_package":
                    col = TASK_COLOR_PACKAGE
                elif task == "do_populate_sysroot":
                    col = TASK_COLOR_SYSROOT

                draw_rect(ctx, PROC_BORDER_COLOR, (x, y, w, proc_h))
                if col:
                    draw_fill_rect(ctx, col, (x, y, w, proc_h))

                draw_label_in_box(ctx, PROC_TEXT_COLOR, val, x, y + proc_h - 4, w, proc_h)
                y = y + proc_h

	# draw process boxes
	#draw_process_bar_chart(ctx, proc_tree, curr_y + bar_h, w, h)

	ctx.set_font_size(SIG_FONT_SIZE)
	draw_text(ctx, SIGNATURE, SIG_COLOR, off_x + 5, h - off_y - 5)

def draw_process_bar_chart(ctx, proc_tree, curr_y, w, h):

	for root in proc_tree.process_tree:        
		draw_processes_recursively(ctx, root, proc_tree, y, proc_h, chart_rect)
		y  = y + proc_h * proc_tree.num_nodes([root])


def draw_header(ctx, headers, off_x, duration):
    dur = duration / 100.0
    toshow = [
      ('system.uname', 'uname', lambda s: s),
      ('system.release', 'release', lambda s: s),
      ('system.cpu', 'CPU', lambda s: re.sub('model name\s*:\s*', '', s, 1)),
      ('system.kernel.options', 'kernel options', lambda s: s),
      ('pseudo.header', 'time', lambda s: '%02d:%05.2f' % (math.floor(dur/60), dur - 60 * math.floor(dur/60)))
    ]

    header_y = ctx.font_extents()[2] + 10
    ctx.set_font_size(TITLE_FONT_SIZE)
    draw_text(ctx, headers['title'], TEXT_COLOR, off_x, header_y)
    ctx.set_font_size(TEXT_FONT_SIZE)
	
    for (headerkey, headertitle, mangle) in toshow:
        header_y += ctx.font_extents()[2]
        txt = headertitle + ': ' + mangle(headers.get(headerkey))
        draw_text(ctx, txt, TEXT_COLOR, off_x, header_y)

    return header_y

def draw_processes_recursively(ctx, proc, proc_tree, y, proc_h, rect) :
	x = rect[0] +  ((proc.start_time - proc_tree.start_time) * rect[2] / proc_tree.duration)
	w = ((proc.duration) * rect[2] / proc_tree.duration)

	draw_process_activity_colors(ctx, proc, proc_tree, x, y, w, proc_h, rect)
	draw_rect(ctx, PROC_BORDER_COLOR, (x, y, w, proc_h))
	draw_label_in_box(ctx, PROC_TEXT_COLOR, proc.cmd, x, y + proc_h - 4, w, rect[0] + rect[2])

	next_y = y + proc_h
	for child in proc.child_list:
		child_x, child_y = draw_processes_recursively(ctx, child, proc_tree, next_y, proc_h, rect)
		draw_process_connecting_lines(ctx, x, y, child_x, child_y, proc_h)
		next_y = next_y + proc_h * proc_tree.num_nodes([child])
		
	return x, y


def draw_process_activity_colors(ctx, proc, proc_tree, x, y, w, proc_h, rect):
	draw_fill_rect(ctx, PROC_COLOR_S, (x, y, w, proc_h))

	last_tx = -1
	for sample in proc.samples :    
		tx = rect[0] + round(((sample.time - proc_tree.start_time) * rect[2] / proc_tree.duration))
		tw = round(proc_tree.sample_period * rect[2] / float(proc_tree.duration))
		if last_tx != -1 and abs(last_tx - tx) <= tw:
			tw -= last_tx - tx
			tx = last_tx
             
		last_tx = tx + tw
		state = get_proc_state( sample.state )

		color = STATE_COLORS[state]        
		if state == STATE_RUNNING:
			alpha = sample.cpu_sample.user + sample.cpu_sample.sys
			color = tuple(list(PROC_COLOR_R[0:3]) + [alpha])
		elif state == STATE_SLEEPING:
			continue

		draw_fill_rect(ctx, color, (tx, y, tw, proc_h))

    
def draw_process_connecting_lines(ctx, px, py, x, y, proc_h):
	ctx.set_source_rgba(*DEP_COLOR)
	ctx.set_dash([2,2])
	if abs(px - x) < 3:
		dep_off_x = 3
		dep_off_y = proc_h / 4
		ctx.move_to(x, y + proc_h / 2)
		ctx.line_to(px - dep_off_x, y + proc_h / 2)
		ctx.line_to(px - dep_off_x, py - dep_off_y)
		ctx.line_to(px, py - dep_off_y)		
	else:
		ctx.move_to(x, y + proc_h / 2)
		ctx.line_to(px, y + proc_h / 2)
		ctx.line_to(px, py)
	ctx.stroke()
        ctx.set_dash([])
