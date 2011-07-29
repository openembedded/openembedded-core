SUMMARY = "Base utilities for working with SGML and XML"
DESCRIPTION = "The sgml-common package gathers very basic \
stuff necessary to work with SGML and XML, such as xml.dcl, \
a SGML declaration of XML; iso-entities, a list of the basic \
SGML ISO entities; and install-catalog, a script used to \
add entries to (or remove entries from) centralized catalogs \
whose entries are pointers to SGML open catalogs, \
as defined by OASIS."
HOMEPAGE = "http://sources.redhat.com/docbook-tools/"
LICENSE = "GPLv2+"
# See the comments in license.patch when upgrading this recipe.
# This is inteded to be a temporary workaround.
LIC_FILES_CHKSUM = "file://LICENSE-GPLv2;md5=ab8a50abe86dfc859e148baae043c89b"
SECTION = "base"

PR = "r1"

require sgml-common_${PV}.bb
inherit native

S = "${WORKDIR}/sgml-common-${PV}"

SYSROOT_PREPROCESS_FUNCS += "sgml_common_native_mangle"
SSTATEPOSTINSTFUNCS += "sgml_common_sstate_postinst"


do_install_append() {
	# install-catalog script contains hard-coded references to
	# {sysconfdir}. Change it to ${D}${sysconfdir}.
	sed -i -e "s|${sysconfdir}/sgml|${D}${sysconfdir}/sgml|g" ${D}${bindir}/install-catalog

	${D}${bindir}/install-catalog \
		--add ${D}${sysconfdir}/sgml/sgml-ent.cat \
		${D}${datadir}/sgml/sgml-iso-entities-8879.1986/catalog

	${D}${bindir}/install-catalog \
		--add ${D}${sysconfdir}/sgml/sgml-docbook.cat \
		${D}${sysconfdir}/sgml/sgml-ent.cat
}

sgml_common_native_mangle () {
	# Revert back to ${sysconfdir} path in install-catalog
	sed -i -e "s|${D}${sysconfdir}/sgml|${sysconfdir}/sgml|g" ${SYSROOT_DESTDIR}${STAGING_BINDIR}/install-catalog
	# Change path from ${D}${datadir}/sgml/sgml-iso-entities-8879.1986/catalog to ${datadir}/sgml/sgml-iso-entities-8879.1986/catalog in sgml-ent.cat
	sed -i -e "s|${D}${datadir}/sgml/sgml-iso-entities-8879.1986/catalog|${datadir}/sgml/sgml-iso-entities-8879.1986/catalog|g" ${SYSROOT_DESTDIR}${sysconfdir}/sgml/sgml-ent.cat
	# Change path from ${D}${sysconfdir}/sgml/sgml-ent.cat|${sysconfdir}/sgml/sgml-ent.cat to ${sysconfdir}/sgml/sgml-ent.cat in sgml-ent.cat
	sed -i -e "s|${D}${sysconfdir}/sgml/sgml-ent.cat|${sysconfdir}/sgml/sgml-ent.cat|g" ${SYSROOT_DESTDIR}${sysconfdir}/sgml/sgml-docbook.cat
	# Remove ${D} path from catalog file created by install-catalog script
	sed -i -e "s|${D}||g" ${SYSROOT_DESTDIR}${sysconfdir}/sgml/catalog
}

sgml_common_sstate_postinst() {
	if [ "${BB_CURRENTTASK}" = "populate_sysroot" -o "${BB_CURRENTTASK}" = "populate_sysroot_setscene" ]
	then
		if [ -e ${sysconfdir}/sgml/sgml-docbook.bak ]; then
			for catalog in `awk '{print $2}' ${sysconfdir}/sgml/sgml-docbook.bak`; do
				if [ ! `grep $catalog ${sysconfdir}/sgml/sgml-docbook.cat 1> /dev/null 2>&1` ]; then
					${bindir}/install-catalog \
						--add ${sysconfdir}/sgml/sgml-docbook.cat $catalog
				fi
			done
		fi
	fi
}
