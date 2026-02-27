SUMMARY = "Simple Xserver Init Script and user account"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
SECTION = "x11"

SRC_URI = "file://emptty.conf.in \
           file://system-xuser.conf"

S = "${UNPACKDIR}"

inherit features_check useradd

REQUIRED_DISTRO_FEATURES = "x11"

PACKAGECONFIG ??= "blank"
# dpms and screen saver will be on only if 'blank' is in PACKAGECONFIG
PACKAGECONFIG[blank] = ""
PACKAGECONFIG[nocursor] = ""

# matchbox-session seems to be the current preferred session provider
DEFAULT_SESSION ??= "matchbox-session"

do_install() {
    install -D -p -m0644 ${S}/emptty.conf.in ${D}${sysconfdir}/emptty/conf

    BLANK_ARGS="${@bb.utils.contains('PACKAGECONFIG', 'blank', '', '-s 0 -dpms', d)}"
    NO_CURSOR_ARG="${@bb.utils.contains('PACKAGECONFIG', 'nocursor', '-nocursor', '', d)}"

    sed -i "s:@NO_CURSOR_ARG@:${NO_CURSOR_ARG}:" ${D}${sysconfdir}/emptty/conf
    sed -i "s:@BLANK_ARGS@:${BLANK_ARGS}:" ${D}${sysconfdir}/emptty/conf
    sed -i "s:@DEFAULT_SESSION@:${DEFAULT_SESSION}:" ${D}${sysconfdir}/emptty/conf

    install -D -m 0644 ${S}/system-xuser.conf ${D}${sysconfdir}/dbus-1/system.d/system-xuser.conf
}

FILES:${PN} = "${sysconfdir}/emptty/conf \
               ${sysconfdir}/dbus-1/system.d/system-xuser.conf"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--create-home \
                       --groups video,tty,audio,input,shutdown,disk,nopasswdlogin \
                       --user-group xuser"
GROUPADD_PARAM:${PN} = "-r nopasswdlogin"

RDEPENDS:${PN} = "emptty"
RPROVIDES:${PN} += "virtual-emptty-conf"
