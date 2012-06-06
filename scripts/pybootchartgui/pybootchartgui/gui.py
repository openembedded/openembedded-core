import gobject
import gtk
import gtk.gdk
import gtk.keysyms

import draw

class PyBootchartWidget(gtk.DrawingArea):
	__gsignals__ = {
		'expose-event': 'override',
		'clicked' : (gobject.SIGNAL_RUN_LAST, gobject.TYPE_NONE, (gobject.TYPE_STRING, gtk.gdk.Event)),
		'position-changed' : (gobject.SIGNAL_RUN_LAST, gobject.TYPE_NONE, (gobject.TYPE_INT, gobject.TYPE_INT)),
		'set-scroll-adjustments' : (gobject.SIGNAL_RUN_LAST, gobject.TYPE_NONE, (gtk.Adjustment, gtk.Adjustment))
	}

	def __init__(self, res):
		gtk.DrawingArea.__init__(self)

		self.res = res

		self.set_flags(gtk.CAN_FOCUS)

                self.add_events(gtk.gdk.BUTTON_PRESS_MASK | gtk.gdk.BUTTON_RELEASE_MASK)
                self.connect("button-press-event", self.on_area_button_press)
                self.connect("button-release-event", self.on_area_button_release)
                self.add_events(gtk.gdk.POINTER_MOTION_MASK | gtk.gdk.POINTER_MOTION_HINT_MASK | gtk.gdk.BUTTON_RELEASE_MASK)
                self.connect("motion-notify-event", self.on_area_motion_notify)
                self.connect("scroll-event", self.on_area_scroll_event)
                self.connect('key-press-event', self.on_key_press_event)

		self.connect('set-scroll-adjustments', self.on_set_scroll_adjustments)
		self.connect("size-allocate", self.on_allocation_size_changed)
		self.connect("position-changed", self.on_position_changed)

		self.zoom_ratio = 1.0
                self.x, self.y = 0.0, 0.0

		self.chart_width, self.chart_height = draw.extents(res)
		self.hadj = None
		self.vadj = None

	def do_expose_event(self, event):
		cr = self.window.cairo_create()

		# set a clip region for the expose event
		cr.rectangle(
			event.area.x, event.area.y,
			event.area.width, event.area.height
		)
		cr.clip()
		self.draw(cr, self.get_allocation())
		return False
		
	def draw(self, cr, rect):	
		cr.set_source_rgba(1.0, 1.0, 1.0, 1.0)
		cr.paint()
                cr.scale(self.zoom_ratio, self.zoom_ratio)
                cr.translate(-self.x, -self.y)
		draw.render(cr, self.res)

	def position_changed(self):
		self.emit("position-changed", self.x, self.y)

	ZOOM_INCREMENT = 1.25

        def zoom_image(self, zoom_ratio):
            self.zoom_ratio = zoom_ratio
	    self._set_scroll_adjustments(self.hadj, self.vadj)
            self.queue_draw()

        def zoom_to_rect(self, rect):
            zoom_ratio = float(rect.width)/float(self.chart_width)
            self.zoom_image(zoom_ratio)
	    self.x = 0
	    self.position_changed()

	def on_zoom_in(self, action):
            self.zoom_image(self.zoom_ratio * self.ZOOM_INCREMENT)

	def on_zoom_out(self, action):
            self.zoom_image(self.zoom_ratio / self.ZOOM_INCREMENT)

	def on_zoom_fit(self, action):		
            self.zoom_to_rect(self.get_allocation())

	def on_zoom_100(self, action):
            self.zoom_image(1.0)

        POS_INCREMENT = 100

        def on_key_press_event(self, widget, event):
                if event.keyval == gtk.keysyms.Left:
                        self.x -= self.POS_INCREMENT/self.zoom_ratio
                elif event.keyval == gtk.keysyms.Right:
                        self.x += self.POS_INCREMENT/self.zoom_ratio
                elif event.keyval == gtk.keysyms.Up:
                        self.y -= self.POS_INCREMENT/self.zoom_ratio
                elif event.keyval == gtk.keysyms.Down:
                        self.y += self.POS_INCREMENT/self.zoom_ratio
                elif event.keyval == gtk.keysyms.Page_Up:
                        self.zoom_image(self.zoom_ratio * self.ZOOM_INCREMENT)
                elif event.keyval == gtk.keysyms.Page_Down:
                        self.zoom_image(self.zoom_ratio / self.ZOOM_INCREMENT)
                else:
                        return False                
                self.queue_draw()
		self.position_changed()
                return True

        def on_area_button_press(self, area, event):
                if event.button == 2 or event.button == 1:
                        area.window.set_cursor(gtk.gdk.Cursor(gtk.gdk.FLEUR))
                        self.prevmousex = event.x
                        self.prevmousey = event.y
                if event.type not in (gtk.gdk.BUTTON_PRESS, gtk.gdk.BUTTON_RELEASE):
                        return False
                return False

        def on_area_button_release(self, area, event):
                if event.button == 2 or event.button == 1:
                        area.window.set_cursor(gtk.gdk.Cursor(gtk.gdk.ARROW))
                        self.prevmousex = None
                        self.prevmousey = None
                        return True
                return False

        def on_area_scroll_event(self, area, event):
                if event.direction == gtk.gdk.SCROLL_UP:
                        self.zoom_image(self.zoom_ratio * self.ZOOM_INCREMENT)
                        return True
                if event.direction == gtk.gdk.SCROLL_DOWN:
                        self.zoom_image(self.zoom_ratio / self.ZOOM_INCREMENT)
                        return True
                return False

        def on_area_motion_notify(self, area, event):
                state = event.state
                if state & gtk.gdk.BUTTON2_MASK or state & gtk.gdk.BUTTON1_MASK:
                        x, y = int(event.x), int(event.y)
                        # pan the image
                        self.x += (self.prevmousex - x)/self.zoom_ratio
                        self.y += (self.prevmousey - y)/self.zoom_ratio
                        self.queue_draw()
                        self.prevmousex = x
                        self.prevmousey = y
			self.position_changed()
                return True

	def on_set_scroll_adjustments(self, area, hadj, vadj):
		self._set_scroll_adjustments(hadj, vadj)

	def on_allocation_size_changed(self, widget, allocation):
		self.hadj.page_size = allocation.width
		self.hadj.page_increment = allocation.width * 0.9
		self.vadj.page_size = allocation.height
		self.vadj.page_increment = allocation.height * 0.9

	def _set_scroll_adjustments(self, hadj, vadj):
		if hadj == None:
			hadj = gtk.Adjustment(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
		if vadj == None:
			vadj = gtk.Adjustment(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
			
		if self.hadj != None and hadj != self.hadj:
			self.hadj.disconnect(self.hadj_changed_signal_id)
		if self.vadj != None and vadj != self.vadj:
			self.vadj.disconnect(self.vadj_changed_signal_id)

		if hadj != None:
			self.hadj = hadj
			self._set_adj_upper(self.hadj, self.zoom_ratio * self.chart_width)
			self.hadj_changed_signal_id = self.hadj.connect('value-changed', self.on_adjustments_changed)

		if vadj != None:
			self.vadj = vadj
			self._set_adj_upper(self.vadj, self.zoom_ratio * self.chart_height)
			self.vadj_changed_signal_id = self.vadj.connect('value-changed', self.on_adjustments_changed)

	def _set_adj_upper(self, adj, upper):
		changed = False
		value_changed = False

		if adj.upper != upper:
			adj.upper = upper
			changed = True

		max_value = max(0.0, upper - adj.page_size)
		if adj.value > max_value:
			adj.value = max_value
			value_changed = True

		if changed:
			adj.changed()
		if value_changed:
			adj.value_changed()

	def on_adjustments_changed(self, adj):
		self.x = self.hadj.value / self.zoom_ratio
		self.y = self.vadj.value / self.zoom_ratio
		self.queue_draw()

	def on_position_changed(self, widget, x, y):
		self.hadj.value = x * self.zoom_ratio
		self.vadj.value = y * self.zoom_ratio

PyBootchartWidget.set_set_scroll_adjustments_signal('set-scroll-adjustments')

class PyBootchartWindow(gtk.Window):

	ui = '''
	<ui>
		<toolbar name="ToolBar">
			<toolitem action="ZoomIn"/>
			<toolitem action="ZoomOut"/>
			<toolitem action="ZoomFit"/>
			<toolitem action="Zoom100"/>
		</toolbar>
	</ui>
	'''

	def __init__(self, res):
		gtk.Window.__init__(self)

		window = self
		window.set_title('Bootchart')
		window.set_default_size(512, 512)
		vbox = gtk.VBox()
		window.add(vbox)

		self.widget = PyBootchartWidget(res)

		# Create a UIManager instance
		uimanager = self.uimanager = gtk.UIManager()
		
		# Add the accelerator group to the toplevel window
		accelgroup = uimanager.get_accel_group()
		window.add_accel_group(accelgroup)

		# Create an ActionGroup
		actiongroup = gtk.ActionGroup('Actions')
		self.actiongroup = actiongroup

		# Create actions
		actiongroup.add_actions((
			('ZoomIn', gtk.STOCK_ZOOM_IN, None, None, None, self.widget.on_zoom_in),
			('ZoomOut', gtk.STOCK_ZOOM_OUT, None, None, None, self.widget.on_zoom_out),
			('ZoomFit', gtk.STOCK_ZOOM_FIT, 'Fit Width', None, None, self.widget.on_zoom_fit),
			('Zoom100', gtk.STOCK_ZOOM_100, None, None, None, self.widget.on_zoom_100),
		))

		# Add the actiongroup to the uimanager
		uimanager.insert_action_group(actiongroup, 0)

		# Add a UI description
		uimanager.add_ui_from_string(self.ui)

		# Scrolled window
                scrolled = gtk.ScrolledWindow()
		scrolled.add(self.widget)

		# Create a Toolbar
		toolbar = uimanager.get_widget('/ToolBar')
		vbox.pack_start(toolbar, False)
		vbox.pack_start(scrolled)

		self.set_focus(self.widget)

		self.show_all()

def show(res):
	win = PyBootchartWindow(res)
	win.connect('destroy', gtk.main_quit)
	gtk.main()
