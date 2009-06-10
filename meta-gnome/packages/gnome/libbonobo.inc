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

ACTIVATION_HEADERS = "Bonobo_Unknown.h Bonobo_GenericFactory.h Bonobo_Activation_types.h \
	              bonobo-activation.h bonobo-activation-async.h bonobo-activation-activate.h \
 		      bonobo-activation-init.h bonobo-activation-shlib.h bonobo-activation-register.h \
 		      bonobo-activation-server-info.h bonobo-activation-version.h"

BONOBO_HEADERS = "Bonobo.h bonobo-arg.h bonobo-context.h bonobo-event-source.h bonobo-exception.h \
	          bonobo-generic-factory.h bonobo-item-container.h bonobo-item-handler.h \
	          bonobo-listener.h bonobo-main.h bonobo-macros.h bonobo-moniker-extender.h \
		  bonobo-moniker-simple.h bonobo-moniker-util.h bonobo-moniker.h bonobo-object.h \
		  bonobo-foreign-object.h bonobo-persist-file.h bonobo-persist-stream.h \
		  bonobo-persist.h bonobo-persist-client.h bonobo-property-bag.h \
	          bonobo-property-bag-client.h bonobo-shlib-factory.h  bonobo-storage.h \
		  bonobo-stream.h bonobo-stream-client.h bonobo-stream-memory.h \
	          bonobo-storage-memory.h bonobo-xobject.h bonobo-i18n.h bonobo-types.h \
		  bonobo-app-client.h bonobo-application.h"

do_compile() {
	oe_runmake ORBIT_IDL="${ORBIT_IDL_SRC}"
}

do_stage() {
	install -d ${STAGING_INCDIR}/bonobo-activation-2.0/bonobo-activation
	for i in ${ACTIVATION_HEADERS}; do install -m 0644 bonobo-activation/$i ${STAGING_INCDIR}/bonobo-activation-2.0/bonobo-activation/; done
	install -d ${STAGING_INCDIR}/libbonobo-2.0/bonobo
	for i in ${BONOBO_HEADERS}; do install -m 0644 bonobo/$i ${STAGING_INCDIR}/libbonobo-2.0/bonobo/; done
	install -m 0644 libbonobo.h ${STAGING_INCDIR}/libbonobo-2.0/
	install -d ${STAGING_DATADIR}/idl/bonobo-activation-2.0/
	install idl/*.idl ${STAGING_DATADIR}/idl/bonobo-activation-2.0/
	oe_libinstall -so -C bonobo libbonobo-2 ${STAGING_LIBDIR}
	oe_libinstall -so -C bonobo-activation libbonobo-activation ${STAGING_LIBDIR}
}

do_install() {
	oe_runmake ORBIT_IDL="${ORBIT_IDL_SRC}" DESTDIR="${D}" install
}
