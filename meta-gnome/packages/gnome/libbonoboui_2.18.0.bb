LICENSE = "GPL"
SECTION = "x11/gnome/libs"
PR = "r1"

inherit gnome pkgconfig

FILES_${PN} += "${libdir}/libglade/2.0/*.so"
FILES_${PN}-dev += "${libdir}/libglade/2.0/* ${datadir}/gnome-2.0/ui \
	${libdir}/bonobo-2.0/samples"
FILES_${PN}-dbg += "${libdir}/bonobo-2.0/samples/.debug \
	${libdir}/libglade/2.0/.debug"

DEPENDS = "libgnomecanvas libbonobo libgnome glib-2.0 gtk-doc gconf libxml2 libglade"

EXTRA_OECONF = "--disable-gtk-doc"

HEADERS = " \
bonobo/bonobo-zoomable.h \
bonobo/bonobo-ui-component.h \
bonobo/bonobo-dock-layout.h \
bonobo/bonobo-ui-type-builtins.h \
bonobo/bonobo-canvas-component.h \
bonobo/bonobo-widget.h \
bonobo/bonobo-ui-engine.h \
bonobo/bonobo-window.h \
bonobo/bonobo-ui-toolbar.h \
bonobo/bonobo-dock-band.h \
bonobo/bonobo-ui-toolbar-item.h \
bonobo/bonobo-control.h \
bonobo/bonobo-dock-item.h \
bonobo/bonobo-ui-config-widget.h \
bonobo/bonobo-zoomable-frame.h \
bonobo/bonobo-control-frame.h \
bonobo/bonobo-dock.h \
bonobo/bonobo-ui-main.h \
bonobo/bonobo-canvas-item.h \
bonobo/bonobo-ui-node.h \
bonobo/bonobo-socket.h \
bonobo/bonobo-selector.h \
bonobo/bonobo-ui-sync.h \
bonobo/bonobo-ui-util.h \
bonobo/bonobo-plug.h \
bonobo/bonobo-ui-toolbar-button-item.h \
bonobo/bonobo-ui-toolbar-toggle-button-item.h \
bonobo/bonobo-ui-container.h \
bonobo/bonobo-file-selector-util.h \
bonobo/bonobo-property-control.h \
bonobo/bonobo-selector-widget.h \
libbonoboui.h \
bonobo.h \
"

do_stage() {
	install -d ${STAGING_INCDIR}/libbonoboui-2.0/bonobo
	for i in ${HEADERS}; do
		install -m 0644 $i ${STAGING_INCDIR}/libbonoboui-2.0/$i
	done
	oe_libinstall -C bonobo -a -so libbonoboui-2 ${STAGING_LIBDIR}
}
