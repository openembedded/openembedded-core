DESCRIPTION = "Simple Xserver Init Script (no dm)"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"
SECTION = "x11"
PRIORITY = "optional"
PR = "r24"
RDEPENDS_${PN} = "dbus-wait sudo"

SRC_URI = "file://xserver-nodm \
           file://Xusername \
           file://gplv2-license.patch"

S = ${WORKDIR}

PACKAGE_ARCH = "all"

do_install() {
    install -d ${D}/etc
    install -d ${D}/etc/init.d
    install xserver-nodm ${D}/etc/init.d
    if [ "${ROOTLESS_X}" = "1" ] ; then
        install -d ${D}/etc/X11
        install Xusername ${D}/etc/X11
    fi
}

pkg_postinst_${PN} () {
    if [ "x$D" != "x" ] ; then
        exit 1
    fi

    if [ -f /etc/X11/Xusername ]; then
        # create the rootless X user, and add user to group tty, video
        username=`cat /etc/X11/Xusername`
        adduser --disabled-password $username
        # FIXME: use addgroup if busybox addgroup is ready
        sed -i -e "s/^video:.*/&${username}/g" /etc/group
        sed -i -e "s/^tty:.*/&${username}/g" /etc/group
    fi
}

inherit update-rc.d

INITSCRIPT_NAME = "xserver-nodm"
INITSCRIPT_PARAMS = "start 9 5 2 . stop 20 0 1 6 ."
