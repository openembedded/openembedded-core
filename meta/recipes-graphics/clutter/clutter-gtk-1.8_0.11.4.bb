require recipes-graphics/clutter/clutter-gtk.inc

SRC_URI = "http://source.clutter-project.org/sources/clutter-gtk/0.11/clutter-gtk-${PV}.tar.bz2"
SRC_URI[md5sum] = "8b88cfcb0358ecfe3e9228efd55f7ebc"
SRC_URI[sha256sum] = "58d5b027c4589ff442d820f624d7991e25cbeaab795b05f2df2e4c7bc1af2cf6"

DEPENDS += "clutter-1.8"

inherit gettext

S = "${WORKDIR}/clutter-gtk-${PV}"