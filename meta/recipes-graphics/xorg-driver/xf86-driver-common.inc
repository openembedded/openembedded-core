DESCRIPTION = "X driver"
HOMEPAGE = "http://www.x.org"
BUGTRACKER = "https://bugs.freedesktop.org"
SECTION = "x11/drivers"
LICENSE = "MIT-X"

PE = "1"
PR = "r1"

DEPENDS = "virtual/xserver-xf86 xproto randrproto util-macros"

XORG_PN = "${BPN}"
SRC_URI = "${XORG_MIRROR}/individual/driver/${XORG_PN}-${PV}.tar.bz2"

S = "${WORKDIR}/${XORG_PN}-${PV}"

FILES_${PN} += " ${libdir}/xorg/modules/drivers/*.so"
FILES_${PN}-dbg += " ${libdir}/xorg/modules/drivers/.debug"

inherit autotools pkgconfig

# AC_CHECK_FILE doesn't work when cross compiling, so we create a replacement
# macro that simply assumes the test succeeds. 
do_configure_prepend () {
    echo 'AC_DEFUN(CC_AC_CHECK_FILE, $2)' > configure.ac.new
    sed 's/AC_CHECK_FILE/CC_AC_CHECK_FILE/g' configure.ac >> configure.ac.new
    mv configure.ac.new configure.ac
}

do_install_append () {
    # driver's la files are not packaged
    rm -f ${D}${libdir}/xorg/modules/drivers/*.la
}
