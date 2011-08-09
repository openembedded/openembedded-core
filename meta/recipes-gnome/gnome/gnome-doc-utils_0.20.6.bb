require gnome-doc-utils.inc
LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=eb723b61539feef013de476e68b5c50a \
		    file://COPYING.LGPL;md5=a6f89e2100d9b6cdffcea4f398e37343"
PR = "r5"

SRC_URI += "file://xsltproc_nonet.patch \
	    file://use-usr-bin-env-for-python-in-xml2po.patch"

SRC_URI[archive.md5sum] = "8f6e05071599bc073007830ea0a68391"
SRC_URI[archive.sha256sum] = "091486e370480bf45349ad09dac799211092a02938b26a0d68206172cb6cebbf"
