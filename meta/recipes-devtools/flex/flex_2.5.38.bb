require flex.inc
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=e4742cf92e89040b39486a6219b68067"
BBCLASSEXTEND = "native nativesdk"

SRC_URI += "file://do_not_create_pdf_doc.patch"

SRC_URI[md5sum] = "b230c88e65996ff74994d08a2a2e0f27"
SRC_URI[sha256sum] = "d42a67ce11b649ce977c8e239d04b2ef1bdbffebb8a79f4f65211ad295f274ec"
