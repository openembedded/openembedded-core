SUMMARY = "Basic X11 session"
DESCRIPTION = "Packages required to set up a basic working X11 session"
LICENSE = "MIT"
PR = "r0"

inherit packagegroup

# For backwards compatibility after rename
RPROVIDES_${PN} = "task-core-x11-mini task-core-x11-base"

RDEPENDS_${PN} = "\
    packagegroup-core-x11-xserver \
    packagegroup-core-x11-utils \
    dbus \
    pointercal \
    matchbox-terminal \
    matchbox-wm \
    mini-x-session \
    liberation-fonts \
    "
