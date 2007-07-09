DESCRIPTION = "openmoko-libs is a set of libraries implementing a Gtk+ based application framework for mobile communication applications"
SECTION = "openmoko/libs"
LICENSE = "LGPL"
DEPENDS += "gtk+ eds-dbus libgsmd libxosd"
PV = "0.4+svn${SRCDATE}"
PR = "r0"

inherit openmoko

do_stage() {
	autotools_stage_all
}

PACKAGES =+ "\
  libmokojournal libmokojournal-dev libmokojournal-dbg \
  libmokogsmd libmokogsmd-dev libmokogsmd-dbg \
  libmokocore libmokocore-dev libmokocore-dbg \
  libmokoui libmokoui-dev libmokoui-dbg \
"

FILES_libmokojournal = "${libdir}/libmokojournal.so.*"
FILES_libmokojournal-dev = "${libdir}/libmokojournal.so ${libdir}/libmokojournal.*a ${includedir}/${PN}/libmokojournal"
FILES_libmokojournal-dbg = "${libdir}/.debug/libmokojournal.so.*"

FILES_libmokogsmd = "${libdir}/libmokogsmd.so.*"
FILES_libmokogsmd-dev = "${libdir}/libmokogsmd.so ${libdir}/libmokogsmd.*a ${includedir}/${PN}/libmokogsmd"
FILES_libmokogsmd-dbg = "${libdir}/.debug/libmokogsmd.so.*"

FILES_libmokocore = "${libdir}/libmokocore.so.*"
FILES_libmokocore-dev = "${libdir}/libmokocore.so ${libdir}/libmokocore.*a ${includedir}/${PN}/libmokocore"
FILES_libmokocore-dbg = "${libdir}/.debug/libmokocore.so.*"
 	 
FILES_libmokoui = "${libdir}/libmokoui.so.*"
FILES_libmokoui-dev = "${libdir}/libmokoui.so ${libdir}/libmokoui.*a ${includedir}/${PN}/libmokoui"
FILES_libmokoui-dbg = "${libdir}/.debug/libmokoui.so.*"
  	 
