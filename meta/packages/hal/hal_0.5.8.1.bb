DESCRIPTION = "Hardware Abstraction Layer"
HOMEPAGE = "http://freedesktop.org/Software/hal"
SECTION = "unknown"
LICENSE = "GPL LGPL AFL"

DEPENDS = "dbus-glib  expat libusb"
RDEPENDS += "udev"
#RDEPENDS_hal-device-manager = "python hal python-pygnome"
RRECOMMENDS = "udev-utils"

PR = "r1"

SRC_URI = "http://freedesktop.org/~david/dist/hal-${PV}.tar.gz"

S = "${WORKDIR}/hal-${PV}"

inherit autotools pkgconfig

EXTRA_OECONF = "--with-hwdata=${datadir}/hwdata \
                --with-expat=${STAGING_LIBDIR}/.. \
                --with-dbus-sys=${sysconfdir}/dbus-1/system.d \
                --with-hotplug=${sysconfdir}/hotplug.d \
                --disable-docbook-docs \
                --disable-policy-kit \
                "

do_stage() {
        autotools_stage_all
        install -d ${STAGING_LIBDIR}
        install -m 755 libhal/.libs/libhal.so.1.0.0 ${STAGING_LIBDIR}/libhal.so
        install -m 755 libhal-storage/.libs/libhal-storage.so.1.0.0 ${STAGING_LIBDIR}/libhal-storage.so
}

#PACKAGES += "hal-device-manager"

#FILES_hal-device-manager = " \
#               ${datadir}/hal/device-manager/ \
#               ${bindir}/hal-device-manager"

FILES_${PN} = "${sysconfdir} \
                ${bindir}/lshal \
                ${bindir}/hal-find-by-capability \
                ${bindir}/hal-find-by-property \
                ${bindir}/hal-device  \
                ${bindir}/hal-get-property \
                ${bindir}/hal-set-property  \
                ${sbindir} \
                ${libdir}/libhal.so* \
                ${libdir}/libhal-storage.so* \
                ${libdir}/hal \
                ${libexecdir} \
                ${datadir}/hal/fdi \
                ${datadir}/hal/scripts"
