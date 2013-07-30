require flex.inc
LICENSE="BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=e4742cf92e89040b39486a6219b68067"
BBCLASSEXTEND = "native nativesdk"

SRC_URI += "file://do_not_create_pdf_doc.patch"

SRC_URI[md5sum] = "c75940e1fc25108f2a7b3ef42abdae06"
SRC_URI[sha256sum] = "17aa7b4ebf19a13bc2dff4115b416365c95f090061539a932a68092349ac052a"
