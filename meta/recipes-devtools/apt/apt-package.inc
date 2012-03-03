apt-manpages="doc/apt-cache.8 \
	      doc/apt-cdrom.8 \
	      doc/apt-config.8 \
	      doc/apt-get.8 \
	      doc/apt.8 \
	      doc/apt.conf.5 \
	      doc/apt_preferences.5 \
	      doc/fr/apt-cache.fr.8 \
	      doc/fr/apt-cdrom.fr.8 \
	      doc/fr/apt-config.fr.8 \
	      doc/fr/apt-get.fr.8 \
	      doc/fr/apt.conf.fr.5 \
	      doc/fr/apt_preferences.fr.5 \
	      doc/fr/sources.list.fr.5 \
	      doc/es/apt.es.8 \
	      doc/es/apt-cache.es.8 \
	      doc/es/apt-cdrom.es.8 \
	      doc/es/apt-config.es.8 \
	      doc/es/apt-get.es.8 \
	      doc/es/apt.conf.es.5 \
	      doc/es/apt_preferences.es.5 \
	      doc/es/sources.list.es.5 \
	      doc/pt_BR/apt_preferences.pt_BR.5 \
	      doc/ja/apt-cache.ja.8 \
	      doc/ja/apt-cdrom.ja.8 \
	      doc/ja/apt-get.ja.8 \
	      doc/ja/apt.conf.ja.5 \
	      doc/sources.list.5"
apt-utils-manpages="doc/apt-extracttemplates.1 \
		    doc/apt-sortpkgs.1 \
		    doc/fr/apt-extracttemplates.fr.1 \
		    doc/fr/apt-sortpkgs.fr.1"
#		    doc/fr/apt-ftparchive.fr.1
#		    doc/apt-ftparchive.1

def get_files_apt_doc(d, bb, manpages):
	import re
	manpages = re.sub(r'\bdoc/(\S+)/(\S+)\.\1\.(.)\b', r'${mandir}/\1/man\3/\2.\3', manpages)
	manpages = re.sub(r'\bdoc/(\S+)\.(.)\b', r'${mandir}/man\2/\1.\2', manpages)
	return manpages

def get_commands_apt_doc(d, bb, manpages):
	import os
	s = list()
	__dir_cache__ = list()
	for m in manpages.split():
		dest = get_files_apt_doc(d, bb, m)
		dir = os.path.dirname(dest)
		if not dir in __dir_cache__:
			s.append("install -d ${D}/%s" % dir)
			__dir_cache__.append(dir)
		s.append("install -m 0644 %s ${D}/%s" % (m, dest))
	return "\n".join(s)

PACKAGES += "${PN}-utils ${PN}-utils-doc"
FILES_${PN} = "${bindir}/apt-cdrom ${bindir}/apt-get \
	       ${bindir}/apt-config ${bindir}/apt-cache \
	       ${libdir}/apt ${libdir}/libapt*.so.* \
	       ${localstatedir} ${sysconfdir} \
	       ${libdir}/dpkg"
FILES_${PN}-utils = "${bindir}/apt-sortpkgs ${bindir}/apt-extracttemplates"
FILES_${PN}-doc = "${@get_files_apt_doc(d, bb, d.getVar('apt-manpages', True))} \
		   ${docdir}/apt"
FILES_${PN}-utils-doc = "${@get_files_apt_doc(d, bb, d.getVar('apt-utils-manpages', True))}"
FILES_${PN}-dev = "${libdir}/libapt*.so ${includedir}"

do_install () {
	set -x
	${@get_commands_apt_doc(d, bb, d.getVar('apt-manpages', True))}
	${@get_commands_apt_doc(d, bb, d.getVar('apt-utils-manpages', True))}
	install -d ${D}${bindir}
	install -m 0755 bin/apt-cdrom ${D}${bindir}/
	install -m 0755 bin/apt-get ${D}${bindir}/
	install -m 0755 bin/apt-config ${D}${bindir}/
	install -m 0755 bin/apt-cache ${D}${bindir}/

	install -m 0755 bin/apt-sortpkgs ${D}${bindir}/
	install -m 0755 bin/apt-extracttemplates ${D}${bindir}/

	eval `cat environment.mak | grep ^GLIBC_VER | sed -e's, = ,=,'`
	eval `cat environment.mak | grep ^LIBSTDCPP_VER | sed -e's, = ,=,'`
	oe_libinstall -so -C bin libapt-pkg$GLIBC_VER$LIBSTDCPP_VER ${D}${libdir}/
	ln -sf libapt-pkg$GLIBC_VER$LIBSTDCPP_VER.so ${D}${libdir}/libapt-pkg.so
	oe_libinstall -so -C bin libapt-inst$GLIBC_VER$LIBSTDCPP_VER ${D}${libdir}/
	ln -sf libapt-inst$GLIBC_VER$LIBSTDCPP_VER.so ${D}${libdir}/libapt-inst.so

	install -d ${D}${libdir}/apt/methods
	install -m 0755 bin/methods/* ${D}${libdir}/apt/methods/

	install -d ${D}${libdir}/dpkg/methods/apt
	install -m 0644 dselect/desc.apt ${D}${libdir}/dpkg/methods/apt/ 
	install -m 0644 dselect/names ${D}${libdir}/dpkg/methods/apt/ 
	install -m 0755 dselect/install ${D}${libdir}/dpkg/methods/apt/ 
	install -m 0755 dselect/setup ${D}${libdir}/dpkg/methods/apt/ 
	install -m 0755 dselect/update ${D}${libdir}/dpkg/methods/apt/ 

	install -d ${D}${sysconfdir}/apt
	install -d ${D}${localstatedir}/lib/apt/lists/partial
	install -d ${D}${localstatedir}/cache/apt/archives/partial
	install -d ${D}${docdir}/apt/examples
	install -m 0644 doc/examples/* ${D}${docdir}/apt/examples/

	install -d ${D}${includedir}/apt-pkg/
	install -m 0644 include/apt-pkg/*.h ${D}${includedir}/apt-pkg/
}
