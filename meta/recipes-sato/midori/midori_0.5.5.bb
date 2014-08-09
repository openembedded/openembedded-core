SUMMARY = "A lightweight web browser"
HOMEPAGE = "http://www.twotoasts.de/index.php?/pages/midori_summary.html"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=fbc093901857fcd118f065f900982c24"
DEPENDS = "webkit-gtk libsoup-2.4 openssl python-native python-docutils-native librsvg-native libnotify libunique libxscrnsaver"

SRC_URI = "http://www.midori-browser.org/downloads/${BPN}_${PV}_all_.tar.bz2 \
"

SRC_URI[md5sum] = "b99e87d4b73a4732ed1c1e591f0242ac"
SRC_URI[sha256sum] = "ca69382a285222a86028abebd73fed1976735883027ff0adc094b627789bbd62"

# midori depends on webkit-gtk, and webkit-gtk can NOT be built on
# MIPS64 with n32 ABI. So remove it from mips64 n32 temporarily.
COMPATIBLE_HOST_mips64 = "mips64.*-linux$"

inherit gtk-icon-cache pkgconfig vala pythonnative

do_configure() {
    sed -i -e 's:, shell=False::g' -e s:/usr/X11R6/include::g -e s:/usr/X11R6/lib::g wscript
    ./configure \
            --prefix=${prefix} \
            --bindir=${bindir} \
            --sbindir=${sbindir} \
            --libexecdir=${libexecdir} \
            --datadir=${datadir} \
            --sysconfdir=${sysconfdir} \
            --sharedstatedir=${sharedstatedir} \
            --localstatedir=${localstatedir} \
            --libdir=${libdir} \
            --includedir=${includedir} \
            --infodir=${infodir} \
            --mandir=${mandir} \
            --disable-gtk3 \
            --disable-zeitgeist \
}

PARALLEL_MAKE = ""
TARGET_CC_ARCH += "${LDFLAGS}"

do_install() {
    oe_runmake DESTDIR=${D} install
}

RRECOMMENDS_${PN} += "glib-networking ca-certificates gnome-icon-theme"

FILES_${PN}-dev += "${datadir}/vala/vapi"
