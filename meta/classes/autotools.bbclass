def autotools_dep_prepend(d):
	if d.getVar('INHIBIT_AUTOTOOLS_DEPS', 1):
		return ''

	pn = d.getVar('PN', 1)
	deps = ''

	if pn in ['autoconf-native', 'automake-native', 'help2man-native']:
		return deps
	deps += 'autoconf-native automake-native '

	if not pn in ['libtool', 'libtool-native'] and not pn.endswith("libtool-cross"):
		deps += 'libtool-native '
		if not bb.data.inherits_class('native', d) \
                        and not bb.data.inherits_class('cross', d) \
                        and not d.getVar('INHIBIT_DEFAULT_DEPS', 1):
                    deps += 'libtool-cross '

	return deps + 'gnu-config-native '

EXTRA_OEMAKE = ""

DEPENDS_prepend = "${@autotools_dep_prepend(d)}"

inherit siteinfo

# Space separated list of shell scripts with variables defined to supply test
# results for autoconf tests we cannot run at build time.
export CONFIG_SITE = "${@siteinfo_get_files(d)}"

acpaths = "default"
EXTRA_AUTORECONF = "--exclude=autopoint"

export lt_cv_sys_lib_dlsearch_path_spec = "${libdir} ${base_libdir}"

def autotools_set_crosscompiling(d):
	if not bb.data.inherits_class('native', d):
		return " cross_compiling=yes"
	return ""

def append_libtool_sysroot(d):
	# Only supply libtool sysroot option for non-native packages
	if not bb.data.inherits_class('native', d):
		return '--with-libtool-sysroot=${STAGING_DIR_HOST}'
	return ""

# EXTRA_OECONF_append = "${@autotools_set_crosscompiling(d)}"

CONFIGUREOPTS = " --build=${BUILD_SYS} \
		  --host=${HOST_SYS} \
		  --target=${TARGET_SYS} \
		  --prefix=${prefix} \
		  --exec_prefix=${exec_prefix} \
		  --bindir=${bindir} \
		  --sbindir=${sbindir} \
		  --libexecdir=${libexecdir} \
		  --datadir=${datadir} \
		  --sysconfdir=${sysconfdir} \
		  --sharedstatedir=${sharedstatedir} \
		  --localstatedir=${localstatedir} \
		  --libdir=${libdir} \
		  --includedir=${includedir} \
		  --oldincludedir=${oldincludedir} \
		  --infodir=${infodir} \
		  --mandir=${mandir} \
		  --disable-silent-rules \
		  ${CONFIGUREOPT_DEPTRACK} \
		  ${@append_libtool_sysroot(d)}"
CONFIGUREOPT_DEPTRACK = "--disable-dependency-tracking"


oe_runconf () {
	cfgscript="${S}/configure"
	if [ -x "$cfgscript" ] ; then
		bbnote "Running $cfgscript ${CONFIGUREOPTS} ${EXTRA_OECONF} $@"
		$cfgscript ${CONFIGUREOPTS} ${EXTRA_OECONF} "$@" || bbfatal "oe_runconf failed"
	else
		bbfatal "no configure script found at $cfgscript"
	fi
}

AUTOTOOLS_AUXDIR ?= "${S}"

autotools_do_configure() {
	case ${PN} in
	autoconf*)
	;;
	automake*)
	;;
	*)
		# WARNING: gross hack follows:
		# An autotools built package generally needs these scripts, however only
		# automake or libtoolize actually install the current versions of them.
		# This is a problem in builds that do not use libtool or automake, in the case
		# where we -need- the latest version of these scripts.  e.g. running a build
		# for a package whose autotools are old, on an x86_64 machine, which the old
		# config.sub does not support.  Work around this by installing them manually
		# regardless.
		( for ac in `find ${S} -name configure.in -o -name configure.ac`; do
			rm -f `dirname $ac`/configure
			done )
		if [ -e ${S}/configure.in -o -e ${S}/configure.ac ]; then
			olddir=`pwd`
			cd ${S}
			if [ x"${acpaths}" = xdefault ]; then
				acpaths=
				for i in `find ${S} -maxdepth 2 -name \*.m4|grep -v 'aclocal.m4'| \
					grep -v 'acinclude.m4' | sed -e 's,\(.*/\).*$,\1,'|sort -u`; do
					acpaths="$acpaths -I $i"
				done
			else
				acpaths="${acpaths}"
			fi
			AUTOV=`automake --version |head -n 1 |sed "s/.* //;s/\.[0-9]\+$//"`
			automake --version
			echo "AUTOV is $AUTOV"
			if [ -d ${STAGING_DATADIR_NATIVE}/aclocal-$AUTOV ]; then
				acpaths="$acpaths -I${STAGING_DATADIR_NATIVE}/aclocal-$AUTOV"
			fi
			# The aclocal directory could get modified by other processes 
			# uninstalling data from the sysroot. See Yocto #861 for details.
			# We avoid this by taking a copy here and then files cannot disappear.
			if [ -d ${STAGING_DATADIR}/aclocal ]; then
				mkdir -p ${B}/aclocal-copy/
				# for scratch build this directory can be empty
				# so avoid cp's no files to copy error
				cp -r ${STAGING_DATADIR}/aclocal/. ${B}/aclocal-copy/
				acpaths="$acpaths -I ${B}/aclocal-copy/"
			fi
			# autoreconf is too shy to overwrite aclocal.m4 if it doesn't look
			# like it was auto-generated.  Work around this by blowing it away
			# by hand, unless the package specifically asked not to run aclocal.
			if ! echo ${EXTRA_AUTORECONF} | grep -q "aclocal"; then
				rm -f aclocal.m4
			fi
			if [ -e configure.in ]; then
			  CONFIGURE_AC=configure.in
			else
			  CONFIGURE_AC=configure.ac
			fi
			if ! echo ${EXTRA_OECONF} | grep -q "\-\-disable-nls"; then
			  if grep "^[[:space:]]*AM_GLIB_GNU_GETTEXT" $CONFIGURE_AC >/dev/null; then
			    if grep "sed.*POTFILES" $CONFIGURE_AC >/dev/null; then
			      : do nothing -- we still have an old unmodified configure.ac
			    else
			      bbnote Executing glib-gettextize --force --copy
			      echo "no" | glib-gettextize --force --copy
			    fi
			  else if grep "^[[:space:]]*AM_GNU_GETTEXT" $CONFIGURE_AC >/dev/null; then
                            # We'd call gettextize here if it wasn't so broken...
			    cp ${STAGING_DATADIR}/gettext/config.rpath ${AUTOTOOLS_AUXDIR}/
			    if [ ! -e ${S}/po/Makefile.in.in ]; then
			      cp ${STAGING_DATADIR}/gettext/po/Makefile.in.in ${S}/po/
                            fi
			  fi
			fi
			fi
			mkdir -p m4
			if grep "^[[:space:]]*[AI][CT]_PROG_INTLTOOL" $CONFIGURE_AC >/dev/null; then
			  bbnote Executing intltoolize --copy --force --automake
			  intltoolize --copy --force --automake
			fi
			bbnote Executing autoreconf --verbose --install --force ${EXTRA_AUTORECONF} $acpaths
			autoreconf -Wcross --verbose --install --force ${EXTRA_AUTORECONF} $acpaths || bbfatal "autoreconf execution failed."
			cd $olddir
		fi
	;;
	esac
	if [ -e ${S}/configure ]; then
		oe_runconf
	else
		bbnote "nothing to configure"
	fi
}

autotools_do_install() {
	oe_runmake 'DESTDIR=${D}' install
	# Info dir listing isn't interesting at this point so remove it if it exists.
	if [ -e "${D}${infodir}/dir" ]; then
		rm -f ${D}${infodir}/dir
	fi
}

inherit siteconfig

EXPORT_FUNCTIONS do_configure do_install
