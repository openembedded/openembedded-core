# The DTDs of the various versions have to be installed in parallel and
# should not replace each other. The installation step is common for
# all versions and just differs in ${DTD_VERSION} which is set in each
# version recipe.
#
# The DTDs do have some quirks (see LFS documentation).
SUMMARY = "Document type definitions for verification of SGML data files"
DESCRIPTION = "Document type definitions for verification of SGML data \
files against the DocBook rule set"
HOMEPAGE = "http://www.docbook.org/sgml/"

DEPENDS = "sgml-common-native"

# Note: the upstream sources are not distributed with a license file.
# LICENSE-OASIS is included as a "patch" to workaround this. When
# upgrading this recipe, please verify whether this is still needed.
SRC_URI = "http://www.docbook.org/sgml/${DTD_VERSION}/docbook-${DTD_VERSION}.zip \
           file://LICENSE-OASIS"

# The .zip file extracts to the current dir
S = "${WORKDIR}"

INC_PR = "r0"

SYSROOT_PREPROCESS_FUNCS += "docbook_sgml_dtd_native_mangle"

inherit native

do_install () {
	# Refer to http://www.linuxfromscratch.org/blfs/view/stable/pst/sgml-dtd.html
	# for details.
	install -d -m 755 ${D}${datadir}/sgml/docbook/sgml-dtd-${DTD_VERSION}
	install docbook.cat ${D}${datadir}/sgml/docbook/sgml-dtd-${DTD_VERSION}/catalog
	cp -PpRr *.dtd *.mod *.dcl ${D}${datadir}/sgml/docbook/sgml-dtd-${DTD_VERSION}

	install-catalog \
		--add ${sysconfdir}/sgml/sgml-docbook-dtd-${DTD_VERSION}.cat \
		${D}${datadir}/sgml/docbook/sgml-dtd-${DTD_VERSION}/catalog

	install-catalog \
		--add ${sysconfdir}/sgml/sgml-docbook-dtd-${DTD_VERSION}.cat \
		${sysconfdir}/sgml/sgml-docbook.cat

	# Copy the generated catalog to the image directory for staging.
	install -d ${D}${sysconfdir}/sgml
	cp ${sysconfdir}/sgml/sgml-docbook-dtd-${DTD_VERSION}.cat ${D}${sysconfdir}/sgml/
}

docbook_sgml_dtd_native_mangle () {
	# Remove the image directory path ${D} from the .cat files.
	sed -i -e "s|${D}||g" ${SYSROOT_DESTDIR}${sysconfdir}/sgml/sgml-docbook.cat || true
	sed -i -e "s|${D}||g" ${SYSROOT_DESTDIR}${sysconfdir}/sgml/sgml-docbook-dtd-${DTD_VERSION}.cat
}
