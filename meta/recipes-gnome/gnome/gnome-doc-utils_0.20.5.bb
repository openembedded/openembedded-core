require gnome-doc-utils.inc
LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=eb723b61539feef013de476e68b5c50a \
		    file://COPYING.LGPL;md5=a6f89e2100d9b6cdffcea4f398e37343"
PR = "r2"

SRC_URI += "file://xsltproc_nonet.patch"

SRC_URI[archive.md5sum] = "3aa1f651834714090cdbf898ec090a98"
SRC_URI[archive.sha256sum] = "08d99b8ab813fadd3407873e4a30282debce92e22eb1b45430a8bb9c120e2130"
