#
# BitBake Graphical GTK User Interface
#
# Copyright (C) 2008        Intel Corporation
#
# Authored by Rob Bradford <rob@linux.intel.com>
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License version 2 as
# published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

import gobject
import gtk
import threading
import bb.ui.uihelper

def event_handle_idle_func (eventHandler, build):

  # Consume as many messages as we can in the time available to us
  event = eventHandler.getEvent()
  while event:
      build.handle_event (event)
      event = eventHandler.getEvent()

  return True

class RunningBuildModel (gtk.TreeStore):
    (COL_TYPE, COL_PACKAGE, COL_TASK, COL_MESSAGE, COL_ICON, COL_ACTIVE) = (0, 1, 2, 3, 4, 5)
    def __init__ (self):
        gtk.TreeStore.__init__ (self, 
                                gobject.TYPE_STRING,
                                gobject.TYPE_STRING,
                                gobject.TYPE_STRING,
                                gobject.TYPE_STRING,
                                gobject.TYPE_STRING,
                                gobject.TYPE_BOOLEAN)

class RunningBuild (gobject.GObject):
    __gsignals__ = {
          'build-finished' : (gobject.SIGNAL_RUN_LAST, 
                              gobject.TYPE_NONE,
                              ())
          }
    pids_to_task = {}
    tasks_to_iter = {}

    def __init__ (self):
        gobject.GObject.__init__ (self)
        self.model = RunningBuildModel()

    def handle_event (self, event):
        # Handle an event from the event queue, this may result in updating
        # the model and thus the UI. Or it may be to tell us that the build
        # has finished successfully (or not, as the case may be.)

        parent = None
        pid = 0
        package = None
        task = None

        # If we have a pid attached to this message/event try and get the
        # (package, task) pair for it. If we get that then get the parent iter
        # for the message.
        if event[1].has_key ('pid'):
            pid = event[1]['pid']
            if self.pids_to_task.has_key(pid):
                (package, task) = self.pids_to_task[pid]
                parent = self.tasks_to_iter[(package, task)]

        if event[0].startswith('bb.msg.Msg'):
            # Set a pretty icon for the message based on it's type.
            if (event[0].startswith ('bb.msg.MsgWarn')):
                icon = "dialog-warning"
            elif (event[0].startswith ('bb.msg.MsgErr')):
                icon = "dialog-error"
            else:
                icon = None

            # Ignore the "Running task i of n .." messages
            if (event[1]['_message'].startswith ("Running task")):
                return

            # Add the message to the tree either at the top level if parent is
            # None otherwise as a descendent of a task.
            self.model.append (parent, 
                               (event[0].split()[-1], # e.g. MsgWarn, MsgError
                                package, 
                                task,
                                event[1]['_message'],
                                icon, 
                                False))
        elif event[0].startswith('bb.build.TaskStarted'):
            (package, task) = (event[1]['_package'], event[1]['_task'])

            # Save out this PID.
            self.pids_to_task[pid] = (package,task)

            # Check if we already have this package in our model. If so then
            # that can be the parent for the task. Otherwise we create a new
            # top level for the package.
            if (self.tasks_to_iter.has_key ((package, None))):
                parent = self.tasks_to_iter[(package, None)]
            else:
                parent = self.model.append (None, (None, 
                                                   package, 
                                                   None,
                                                   "Package: %s" % (package), 
                                                   None,
                                                   False))
                self.tasks_to_iter[(package, None)] = parent

            # Because this parent package now has an active child mark it as
            # such.
            self.model.set(parent, self.model.COL_ICON, "gtk-execute")

            # Add an entry in the model for this task
            i = self.model.append (parent, (None, 
                                            package, 
                                            task,
                                            "Task: %s" % (task), 
                                            None,
                                            False))

            # Save out the iter so that we can find it when we have a message
            # that we need to attach to a task.
            self.tasks_to_iter[(package, task)] = i

            # Mark this task as active.
            self.model.set(i, self.model.COL_ICON, "gtk-execute")

        elif event[0].startswith('bb.build.Task'):

            if event[0].startswith('bb.build.TaskFailed'):
                # Mark the task as failed
                i = self.tasks_to_iter[(package, task)]
                self.model.set(i, self.model.COL_ICON, "dialog-error")

                # Mark the parent package as failed
                i = self.tasks_to_iter[(package, None)]
                self.model.set(i, self.model.COL_ICON, "dialog-error")
            else:
                # Mark the task as inactive
                i = self.tasks_to_iter[(package, task)]
                self.model.set(i, self.model.COL_ICON, None)

                # Mark the parent package as inactive
                i = self.tasks_to_iter[(package, None)]
                self.model.set(i, self.model.COL_ICON, None)


            # Clear the iters and the pids since when the task goes away the
            # pid will no longer be used for messages
            del self.tasks_to_iter[(package, task)]
            del self.pids_to_task[pid]

class RunningBuildTreeView (gtk.TreeView):
    def __init__ (self):
        gtk.TreeView.__init__ (self)

        # The icon that indicates whether we're building or failed.
        renderer = gtk.CellRendererPixbuf ()
        col = gtk.TreeViewColumn ("Status", renderer)
        col.add_attribute (renderer, "icon-name", 4)
        self.append_column (col)

        # The message of the build.
        renderer = gtk.CellRendererText ()
        col = gtk.TreeViewColumn ("Message", renderer, text=3)
        self.append_column (col)


class MainWindow (gtk.Window):
    def __init__ (self):
        gtk.Window.__init__ (self, gtk.WINDOW_TOPLEVEL)

        # Setup tree view and the scrolled window
        scrolled_window = gtk.ScrolledWindow ()
        self.add (scrolled_window)
        self.cur_build_tv = RunningBuildTreeView()
        scrolled_window.add (self.cur_build_tv)

def init (server, eventHandler):
    gobject.threads_init()
    gtk.gdk.threads_init()

    window = MainWindow ()
    window.show_all ()

    # Create the object for the current build
    running_build = RunningBuild ()
    window.cur_build_tv.set_model (running_build.model)
    try:
        cmdline = server.runCommand(["getCmdLineAction"])
        print cmdline
        if not cmdline:
            return 1
        ret = server.runCommand(cmdline)
        if ret != True:
            print "Couldn't get default commandline! %s" % ret
            return 1
    except xmlrpclib.Fault, x:
        print "XMLRPC Fault getting commandline:\n %s" % x
        return 1

    # Use a timeout function for probing the event queue to find out if we
    # have a message waiting for us.
    gobject.timeout_add (200,
                         event_handle_idle_func,
                         eventHandler,
                         running_build)

    gtk.main()

