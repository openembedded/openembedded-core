DESCRIPTION = "Bonobo CORBA interfaces library"
LICENSE = "GPL"
SECTION = "x11/gnome/libs"
DEPENDS = "glib-2.0 orbit2 intltool-native libxml2"

inherit gnome

SRC_URI += "file://gtk-doc.m4 \
            file://gtk-doc.make \
           "

ORBIT_IDL_SRC = "${STAGING_BINDIR_NATIVE}/orbit-idl-2"

FILES_${PN} += "${libdir}/orbit-2.0/*.so ${libdir}/bonobo/monikers/*.so"
FILES_${PN}-dbg += "${libdir}/bonobo/monikers/.debug \
	${libdir}/bonobo-2.0/samples/.debug ${libdir}/orbit-2.0/.debug"
FILES_${PN}-dev += "${libdir}/orbit-2.0/* ${libdir}/bonobo/monikers/* \
	${libdir}/bonobo-2.0/samples"

LEAD_SONAME = "libbonobo-2.so"

PARALLEL_MAKE = ""

do_configure_prepend() {
	install -d m4
	install ${WORKDIR}/gtk-doc.m4 m4/
	install ${WORKDIR}/gtk-doc.make ./
}

do_compile() {
	oe_runmake ORBIT_IDL="${ORBIT_IDL_SRC}"
}

do_install() {
	oe_runmake ORBIT_IDL="${ORBIT_IDL_SRC}" DESTDIR="${D}" install
}
