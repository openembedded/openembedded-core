SUMMARY = "Xcomposite: X Composite extension library"

DESCRIPTION = "The composite extension provides three related \
mechanisms: per-hierarchy storage, automatic shadow update, and external \
parent.  In per-hierarchy storage, the rendering of an entire hierarchy \
of windows is redirected to off-screen storage.  In automatic shadow \
update, when a hierarchy is rendered off-screen, the X server provides \
an automatic mechanism for presenting those contents within the parent \
window.  In external parent, a mechanism for providing redirection of \
compositing transformations through a client."

require xorg-lib-common.inc

LICENSE= "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=3f2907aad541f6f226fbc58cc1b3cdf1"

DEPENDS += " compositeproto virtual/libx11 libxfixes libxext"
PROVIDES = "xcomposite"

PE = "1"
PR = "r0"

XORG_PN = "libXcomposite"

SRC_URI += " file://change-include-order.patch"

SRC_URI[md5sum] = "a60e0b5c276d0aa9e2d3b982c98f61c8"
SRC_URI[sha256sum] = "32294d28f4ee46db310c344546d98484728b7d52158c6d7c25bba02563b41aad"
