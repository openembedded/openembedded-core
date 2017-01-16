require expat.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/expat-2.1.0:"
LIC_FILES_CHKSUM = "file://COPYING;md5=1b71f681713d1256e1c23b0890920874"

SRC_URI += "file://CVE-2016-5300_CVE-2012-6702.patch \
           "
SRC_URI[md5sum] = "dd7dab7a5fea97d2a6a43f511449b7cd"
SRC_URI[sha256sum] = "823705472f816df21c8f6aa026dd162b280806838bb55b3432b0fb1fcca7eb86"
