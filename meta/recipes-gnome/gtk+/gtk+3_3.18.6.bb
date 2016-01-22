require gtk+3.inc

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "http://ftp.gnome.org/pub/gnome/sources/gtk+/${MAJ_VER}/gtk+-${PV}.tar.xz \
           file://hardcoded_libtool.patch \
           file://Dont-force-csd.patch \
           file://Do-not-try-to-initialize-GL-without-libGL.patch \
          "

SRC_URI[md5sum] = "e1c41ad4a6012d703a80f7423acf186e"
SRC_URI[sha256sum] = "78cabf0fd5a662f8723f62d5ac633072c76c557c1d700454c9c3deaa37e441ef"

S = "${WORKDIR}/gtk+-${PV}"

LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2 \
                    file://gtk/gtk.h;endline=25;md5=1d8dc0fccdbfa26287a271dce88af737 \
                    file://gdk/gdk.h;endline=25;md5=c920ce39dc88c6f06d3e7c50e08086f2 \
                    file://tests/testgtk.c;endline=25;md5=cb732daee1d82af7a2bf953cf3cf26f1"
