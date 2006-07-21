#
# For now, we will skip building of a gcc package if it is a uclibc one
# and our build is not a uclibc one, and we skip a glibc one if our build
# is a uclibc build.
#
# See the note in gcc/gcc_3.4.0.oe
#

python __anonymous () {
    import bb, re
    uc_os = (re.match('.*uclibc$', bb.data.getVar('TARGET_OS', d, 1)) != None)
    if uc_os:
        raise bb.parse.SkipPackage("incompatible with target %s" %
                                   bb.data.getVar('TARGET_OS', d, 1))
}

# Binary locales are generated at build time if ENABLE_BINARY_LOCALE_GENERATION
# is set. The idea is to avoid running localedef on the target (at first boot)
# to decrease initial boot time and avoid localedef being killed by the OOM
# killer which used to effectively break i18n on machines with < 128MB RAM.

# default to disabled until qemu works for everyone
ENABLE_BINARY_LOCALE_GENERATION ?= "0"

# BINARY_LOCALE_ARCHES is a space separated list of regular expressions
BINARY_LOCALE_ARCHES ?= "arm.*"

PACKAGES = "glibc catchsegv sln nscd ldd localedef glibc-utils glibc-dev glibc-doc glibc-locale libsegfault glibc-extra-nss glibc-thread-db glibc-pcprofile"
PACKAGES_DYNAMIC = "glibc-gconv-* glibc-charmap-* glibc-localedata-*"

libc_baselibs = "/lib/libc* /lib/libm* /lib/ld* /lib/libpthread* /lib/libresolv* /lib/librt* /lib/libutil* /lib/libnsl* /lib/libnss_files* /lib/libnss_compat* /lib/libnss_dns* /lib/libdl* /lib/libanl* /lib/libBrokenLocale*"

FILES_${PN} = "${sysconfdir} ${libc_baselibs} /sbin/ldconfig ${libexecdir} ${datadir}/zoneinfo"
FILES_ldd = "${bindir}/ldd"
FILES_libsegfault = "/lib/libSegFault*"
FILES_glibc-extra-nss = "/lib/libnss*"
FILES_sln = "/sbin/sln"
FILES_glibc-dev_append = " ${libdir}/*.o ${bindir}/rpcgen"
FILES_nscd = "${sbindir}/nscd*"
FILES_glibc-utils = "${bindir} ${sbindir}"
FILES_glibc-gconv = "${libdir}/gconv"
FILES_catchsegv = "${bindir}/catchsegv"
RDEPENDS_catchsegv = "libsegfault"
FILES_glibc-pcprofile = "/lib/libpcprofile.so"
FILES_glibc-thread-db = "/lib/libthread_db*"
FILES_localedef = "${bindir}/localedef"
RPROVIDES_glibc-dev += "libc-dev"

DESCRIPTION_sln = "glibc: create symbolic links between files"
DESCRIPTION_nscd = "glibc: name service cache daemon for passwd, group, and hosts"
DESCRIPTION_glibc-extra-nss = "glibc: nis, nisplus and hesiod search services"
DESCRIPTION_ldd = "glibc: print shared library dependencies"
DESCRIPTION_localedef = "glibc: compile locale definition files"
DESCRIPTION_glibc-utils = "glibc: misc utilities like iconf, local, gencat, tzselect, rpcinfo, ..."

def get_glibc_fpu_setting(bb, d):
	if bb.data.getVar('TARGET_FPU', d, 1) in [ 'soft' ]:
		return "--without-fp"
	return ""

EXTRA_OECONF += "${@get_glibc_fpu_setting(bb, d)}"

OVERRIDES_append = ":${TARGET_ARCH}-${TARGET_OS}"
EXTRA_OECONF_append_arm-linuxeabi = " --without-fp"

do_install() {
	oe_runmake install_root=${D} install
	for r in ${rpcsvc}; do
		h=`echo $r|sed -e's,\.x$,.h,'`
		install -m 0644 ${S}/sunrpc/rpcsvc/$h ${D}/${includedir}/rpcsvc/
	done
	install -m 0644 ${WORKDIR}/etc/ld.so.conf ${D}/${sysconfdir}/
	install -d ${D}${libdir}/locale
	make -f ${WORKDIR}/generate-supported.mk IN="${S}/localedata/SUPPORTED" OUT="${WORKDIR}/SUPPORTED"
	# get rid of some broken files...
	for i in ${GLIBC_BROKEN_LOCALES}; do
		grep -v $i ${WORKDIR}/SUPPORTED > ${WORKDIR}/SUPPORTED.tmp
		mv ${WORKDIR}/SUPPORTED.tmp ${WORKDIR}/SUPPORTED
	done
	rm -f ${D}/etc/rpc
}

TMP_LOCALE="/tmp/locale/${libdir}/locale"

locale_base_postinst() {
#!/bin/sh

if [ "x$D" != "x" ]; then
  exit 1
fi

rm -rf ${TMP_LOCALE}
mkdir -p ${TMP_LOCALE}
if [ -f ${libdir}/locale/locale-archive ]; then
        cp ${libdir}/locale/locale-archive ${TMP_LOCALE}/
fi
localedef --inputfile=${datadir}/i18n/locales/%s --charmap=%s --prefix=/tmp/locale %s
mv ${TMP_LOCALE}/locale-archive ${libdir}/locale/
rm -rf ${TMP_LOCALE}
}

locale_base_postrm() {
#!/bin/sh

rm -rf ${TMP_LOCALE}
mkdir -p ${TMP_LOCALE}
if [ -f ${libdir}/locale/locale-archive ]; then
	cp ${libdir}/locale/locale-archive ${TMP_LOCALE}/
fi
localedef --delete-from-archive --inputfile=${datadir}/locales/%s --charmap=%s --prefix=/tmp/locale %s
mv ${TMP_LOCALE}/locale-archive ${libdir}/locale/
rm -rf ${TMP_LOCALE}
}

python __anonymous () {
	enabled = bb.data.getVar("ENABLE_BINARY_LOCALE_GENERATION", d, 1)

	if enabled and int(enabled):
		import re

		target_arch = bb.data.getVar("TARGET_ARCH", d, 1)
		binary_arches = bb.data.getVar("BINARY_LOCALE_ARCHES", d, 1) or ""

		for regexp in binary_arches.split(" "):
			r = re.compile(regexp)

			if r.match(target_arch):
				depends = bb.data.getVar("DEPENDS", d, 1)
				depends = "%s qemu-native" % depends
				bb.data.setVar("DEPENDS", depends, d)
				bb.data.setVar("GLIBC_INTERNAL_USE_BINARY_LOCALE", "1", d)
				break
}

do_prep_locale_tree() {
	treedir=${WORKDIR}/locale-tree
	rm -rf $treedir
	mkdir -p $treedir/bin $treedir/lib $treedir/${datadir} $treedir/${libdir}/locale
	cp -a ${D}${datadir}/i18n $treedir/${datadir}/i18n
	# unzip to avoid parsing errors
	for i in $treedir/${datadir}/i18n/charmaps/*gz; do 
		gunzip $i
	done
	cp -a ${STAGING_LIBDIR}/* $treedir/lib
	if [ -f ${CROSS_DIR}/${TARGET_SYS}/lib/libgcc_s.* ]; then
		cp -a ${CROSS_DIR}/${TARGET_SYS}/lib/libgcc_s.* $treedir/lib
	fi
	install -m 0755 ${D}${bindir}/localedef $treedir/bin
}

do_collect_bins_from_locale_tree() {
	treedir=${WORKDIR}/locale-tree

	mkdir -p ${D}${libdir}
	cp -a $treedir/${libdir}/locale ${D}${libdir}
}

python package_do_split_gconvs () {
	import os, re
	if (bb.data.getVar('PACKAGE_NO_GCONV', d, 1) == '1'):
		bb.note("package requested not splitting gconvs")
		return

	if not bb.data.getVar('PACKAGES', d, 1):
		return

	libdir = bb.data.getVar('libdir', d, 1)
	if not libdir:
		bb.error("libdir not defined")
		return
	datadir = bb.data.getVar('datadir', d, 1)
	if not datadir:
		bb.error("datadir not defined")
		return

	gconv_libdir = os.path.join(libdir, "gconv")
	charmap_dir = os.path.join(datadir, "i18n", "charmaps")
	locales_dir = os.path.join(datadir, "i18n", "locales")
	binary_locales_dir = os.path.join(libdir, "locale")

	do_split_packages(d, gconv_libdir, file_regex='^(.*)\.so$', output_pattern='glibc-gconv-%s', description='gconv module for character set %s', extra_depends='glibc-gconv')

	do_split_packages(d, charmap_dir, file_regex='^(.*)\.gz$', output_pattern='glibc-charmap-%s', description='character map for %s encoding', extra_depends='')

	def calc_locale_deps(fn, pkg, file_regex, output_pattern, group):
		deps = []
		f = open(fn, "r")
		c_re = re.compile('^copy "(.*)"')
		i_re = re.compile('^include "(\w+)".*')
		for l in f.readlines():
			m = c_re.match(l) or i_re.match(l)
			if m:
				dp = legitimize_package_name('glibc-localedata-%s' % m.group(1))
				if not dp in deps:
					deps.append(dp)
		f.close()
		if deps != []:
			bb.data.setVar('RDEPENDS_%s' % pkg, " ".join(deps), d)

	use_bin = bb.data.getVar("GLIBC_INTERNAL_USE_BINARY_LOCALE", d, 1)
	if use_bin:
		do_split_packages(d, locales_dir, file_regex='(.*)', output_pattern='glibc-localedata-%s', description='locale definition for %s', hook=calc_locale_deps, extra_depends='', aux_files_pattern_verbatim=binary_locales_dir + '/%s')
	else:
		do_split_packages(d, locales_dir, file_regex='(.*)', output_pattern='glibc-localedata-%s', description='locale definition for %s', hook=calc_locale_deps, extra_depends='')
		bb.note("generation of binary locales disabled. this may break i18n!")
	bb.data.setVar('PACKAGES', bb.data.getVar('PACKAGES', d) + ' glibc-gconv', d)

	f = open(os.path.join(bb.data.getVar('WORKDIR', d, 1), "SUPPORTED"), "r")
	supported = f.readlines()
	f.close()

	dot_re = re.compile("(.*)\.(.*)")

	# Collate the locales by base and encoding
	encodings = {}
	for l in supported:
		l = l[:-1]
		(locale, charset) = l.split(" ")
		m = dot_re.match(locale)
		if m:
			locale = m.group(1)
		if not encodings.has_key(locale):
			encodings[locale] = []
		encodings[locale].append(charset)

	def output_locale_source(name, locale, encoding):
		pkgname = 'locale-base-' + legitimize_package_name(name)

		bb.data.setVar('RDEPENDS_%s' % pkgname, 'localedef glibc-localedata-%s glibc-charmap-%s' % (legitimize_package_name(locale), legitimize_package_name(encoding)), d)
		rprovides = 'virtual-locale-%s' % legitimize_package_name(name)
		m = re.match("(.*)_(.*)", name)
		if m:
			rprovides += ' virtual-locale-%s' % m.group(1)
		bb.data.setVar('RPROVIDES_%s' % pkgname, rprovides, d)
		bb.data.setVar('PACKAGES', '%s %s' % (pkgname, bb.data.getVar('PACKAGES', d, 1)), d)
		bb.data.setVar('ALLOW_EMPTY_%s' % pkgname, '1', d)
		bb.data.setVar('pkg_postinst_%s' % pkgname, bb.data.getVar('locale_base_postinst', d, 1) % (locale, encoding, locale), d)
		bb.data.setVar('pkg_postrm_%s' % pkgname, bb.data.getVar('locale_base_postrm', d, 1) % (locale, encoding, locale), d)

	def output_locale_binary(name, locale, encoding):
		target_arch = bb.data.getVar("TARGET_ARCH", d, 1)
		qemu = "qemu-%s" % target_arch
		pkgname = 'locale-base-' + legitimize_package_name(name)

		bb.data.setVar('RDEPENDS_%s' % pkgname, 'glibc-localedata-%s glibc-charmap-%s' % (legitimize_package_name(locale), legitimize_package_name(encoding)), d)
		rprovides = 'virtual-locale-%s' % legitimize_package_name(name)
		m = re.match("(.*)_(.*)", name)
		if m:
			rprovides += ' virtual-locale-%s' % m.group(1)
		bb.data.setVar('RPROVIDES_%s' % pkgname, rprovides, d)
		bb.data.setVar('ALLOW_EMPTY_%s' % pkgname, '1', d)
		bb.data.setVar('PACKAGES', '%s %s' % (pkgname, bb.data.getVar('PACKAGES', d, 1)), d)

		treedir = os.path.join(bb.data.getVar("WORKDIR", d, 1), "locale-tree")
		path = bb.data.getVar("PATH", d, 1)
		i18npath = os.path.join(treedir, datadir, "i18n")

		localedef_opts = "--force --old-style --no-archive --prefix=%s --inputfile=%s/i18n/locales/%s --charmap=%s %s" % (treedir, datadir, locale, encoding, locale)
		cmd = "PATH=\"%s\" I18NPATH=\"%s\" %s -L %s %s/bin/localedef %s" % (path, i18npath, qemu, treedir, treedir, localedef_opts)
		bb.note("generating locale %s (%s)" % (locale, encoding))
		if os.system(cmd):
			raise bb.build.FuncFailed("localedef returned an error.")

	def output_locale(name, locale, encoding):
		use_bin = bb.data.getVar("GLIBC_INTERNAL_USE_BINARY_LOCALE", d, 1)
		if use_bin:
			output_locale_binary(name, locale, encoding)
		else:
			output_locale_source(name, locale, encoding)

	use_bin = bb.data.getVar("GLIBC_INTERNAL_USE_BINARY_LOCALE", d, 1)
	if use_bin:
		bb.note("preparing tree for binary locale generation")
		bb.build.exec_func("do_prep_locale_tree", d)

	# Reshuffle names so that UTF-8 is preferred over other encodings
	for l in encodings.keys():
		if len(encodings[l]) == 1:
			output_locale(l, l, encodings[l][0])
		else:
			if "UTF-8" in encodings[l]:
				output_locale(l, l, "UTF-8")
				encodings[l].remove("UTF-8")
			for e in encodings[l]:
				output_locale('%s-%s' % (l, e), l, e)			

	use_bin = bb.data.getVar("GLIBC_INTERNAL_USE_BINARY_LOCALE", d, 1)
	if use_bin:
		bb.note("collecting binary locales from locale tree")
		bb.build.exec_func("do_collect_bins_from_locale_tree", d)
}

# We want to do this indirection so that we can safely 'return'
# from the called function even though we're prepending
python populate_packages_prepend () {
	if bb.data.getVar('DEBIAN_NAMES', d, 1):
		bb.data.setVar('PKG_glibc', 'libc6', d)
		bb.data.setVar('PKG_glibc-dev', 'libc6-dev', d)
	bb.build.exec_func('package_do_split_gconvs', d)
}
