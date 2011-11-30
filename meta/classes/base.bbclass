BB_DEFAULT_TASK ?= "build"

inherit patch
inherit staging

inherit mirrors
inherit utils
inherit utility-tasks
inherit metadata_scm
inherit logging

OE_IMPORTS += "os sys time oe.path oe.utils oe.data oe.packagegroup"
OE_IMPORTS[type] = "list"

def oe_import(d):
    import os, sys

    bbpath = d.getVar("BBPATH", True).split(":")
    sys.path[0:0] = [os.path.join(dir, "lib") for dir in bbpath]

    def inject(name, value):
        """Make a python object accessible from the metadata"""
        if hasattr(bb.utils, "_context"):
            bb.utils._context[name] = value
        else:
            __builtins__[name] = value

    import oe.data
    for toimport in oe.data.typed_value("OE_IMPORTS", d):
        imported = __import__(toimport)
        inject(toimport.split(".", 1)[0], imported)

python oe_import_eh () {
    if isinstance(e, bb.event.ConfigParsed):
	oe_import(e.data)
}

addhandler oe_import_eh

die() {
	bbfatal "$*"
}

oe_runmake() {
	if [ x"$MAKE" = x ]; then MAKE=make; fi
	bbnote ${MAKE} ${EXTRA_OEMAKE} "$@"
	${MAKE} ${EXTRA_OEMAKE} "$@" || die "oe_runmake failed"
}


def base_dep_prepend(d):
	#
	# Ideally this will check a flag so we will operate properly in
	# the case where host == build == target, for now we don't work in
	# that case though.
	#

	deps = ""
	# INHIBIT_DEFAULT_DEPS doesn't apply to the patch command.  Whether or  not
	# we need that built is the responsibility of the patch function / class, not
	# the application.
	if not d.getVar('INHIBIT_DEFAULT_DEPS'):
		if (d.getVar('HOST_SYS', 1) !=
	     	    d.getVar('BUILD_SYS', 1)):
			deps += " virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}compilerlibs virtual/libc "
	return deps

BASEDEPENDS = "${@base_dep_prepend(d)}"

DEPENDS_prepend="${BASEDEPENDS} "

FILESPATH = "${@base_set_filespath([ "${FILE_DIRNAME}/${PF}", "${FILE_DIRNAME}/${P}", "${FILE_DIRNAME}/${PN}", "${FILE_DIRNAME}/${BP}", "${FILE_DIRNAME}/${BPN}", "${FILE_DIRNAME}/files", "${FILE_DIRNAME}" ], d)}"
# THISDIR only works properly with imediate expansion as it has to run
# in the context of the location its used (:=)
THISDIR = "${@os.path.dirname(d.getVar('FILE', True))}"

addtask fetch
do_fetch[dirs] = "${DL_DIR}"
python base_do_fetch() {

	src_uri = (d.getVar('SRC_URI', True) or "").split()
	if len(src_uri) == 0:
		return

	localdata = bb.data.createCopy(d)
	bb.data.update_data(localdata)

        try:
            fetcher = bb.fetch2.Fetch(src_uri, localdata)
            fetcher.download()
        except bb.fetch2.BBFetchException, e:
            raise bb.build.FuncFailed(e)
}

addtask unpack after do_fetch
do_unpack[dirs] = "${WORKDIR}"
python base_do_unpack() {
	src_uri = (d.getVar('SRC_URI', True) or "").split()
	if len(src_uri) == 0:
		return

	localdata = bb.data.createCopy(d)
	bb.data.update_data(localdata)

	rootdir = localdata.getVar('WORKDIR', True)

        try:
            fetcher = bb.fetch2.Fetch(src_uri, localdata)
            fetcher.unpack(rootdir)
        except bb.fetch2.BBFetchException, e:
            raise bb.build.FuncFailed(e)
}

GIT_CONFIG = "${STAGING_DIR_NATIVE}/usr/etc/gitconfig"

def generate_git_config(e):
        from bb import data

        if data.getVar('GIT_CORE_CONFIG', e.data, True):
                gitconfig_path = e.data.getVar('GIT_CONFIG', True)
                proxy_command = "    gitproxy = %s\n" % data.getVar('GIT_PROXY_COMMAND', e.data, True)

                bb.mkdirhier(bb.data.expand("${STAGING_DIR_NATIVE}/usr/etc/", e.data))
                if (os.path.exists(gitconfig_path)):
                        os.remove(gitconfig_path)

                f = open(gitconfig_path, 'w')
                f.write("[core]\n")
                ignore_hosts = data.getVar('GIT_PROXY_IGNORE', e.data, True).split()
                for ignore_host in ignore_hosts:
                        f.write("    gitproxy = none for %s\n" % ignore_host)
                f.write(proxy_command)
                f.close

def pkgarch_mapping(d):
    # Compatibility mappings of TUNE_PKGARCH (opt in)
    if d.getVar("PKGARCHCOMPAT_ARMV7A", True):
        if d.getVar("TUNE_PKGARCH", True) == "armv7a-vfp-neon":
            d.setVar("TUNE_PKGARCH", "armv7a")

def preferred_ml_updates(d):
    # If any PREFERRED_PROVIDER or PREFERRED_VERSIONS are set,
    # we need to mirror these variables in the multilib case
    multilibs = d.getVar('MULTILIBS', True) or ""
    if not multilibs:
        return

    prefixes = []
    for ext in multilibs.split():
        eext = ext.split(':')
        if len(eext) > 1 and eext[0] == 'multilib':
            prefixes.append(eext[1])

    versions = []
    providers = []
    for v in d.keys():
        if v.startswith("PREFERRED_VERSION_"):
            versions.append(v)
        if v.startswith("PREFERRED_PROVIDER_"):
            providers.append(v)

    for v in versions:
        val = d.getVar(v, False)
        pkg = v.replace("PREFERRED_VERSION_", "")
        if pkg.endswith("-native") or pkg.endswith("-nativesdk"):
            continue
        for p in prefixes:
            newname = "PREFERRED_VERSION_" + p + "-" + pkg
            if not d.getVar(newname, False):
                d.setVar(newname, val)

    for prov in providers:
        val = d.getVar(prov, False)
        pkg = prov.replace("PREFERRED_PROVIDER_", "")
        if pkg.endswith("-native") or pkg.endswith("-nativesdk"):
            continue
        virt = ""
        if pkg.startswith("virtual/"):
             pkg = pkg.replace("virtual/", "")
             virt = "virtual/"
        for p in prefixes:
            newname = "PREFERRED_PROVIDER_" + virt + p + "-" + pkg
            if pkg != "kernel":
                val = p + "-" + val
            if not d.getVar(newname, False):
                d.setVar(newname, val)


    mp = (d.getVar("MULTI_PROVIDER_WHITELIST", True) or "").split()
    extramp = []
    for p in mp:
        if p.endswith("-native") or p.endswith("-nativesdk"):
            continue
        virt = ""
        if p.startswith("virtual/"):
            p = p.replace("virtual/", "")
            virt = "virtual/"
        for pref in prefixes:
            extramp.append(virt + pref + "-" + p)
    d.setVar("MULTI_PROVIDER_WHITELIST", " ".join(mp + extramp))


def get_layers_branch_rev(d):
	layers = (d.getVar("BBLAYERS", 1) or "").split()
	layers_branch_rev = ["%-17s = \"%s:%s\"" % (os.path.basename(i), \
		base_get_metadata_git_branch(i, None).strip(), \
		base_get_metadata_git_revision(i, None)) \
			for i in layers]
	i = len(layers_branch_rev)-1
	p1 = layers_branch_rev[i].find("=")
	s1 = layers_branch_rev[i][p1:]
	while i > 0:
		p2 = layers_branch_rev[i-1].find("=")
		s2= layers_branch_rev[i-1][p2:]
		if s1 == s2:
			layers_branch_rev[i-1] = layers_branch_rev[i-1][0:p2]
			i -= 1
		else:
			i -= 1
			p1 = layers_branch_rev[i].find("=")
			s1= layers_branch_rev[i][p1:]
	return layers_branch_rev


addhandler base_eventhandler
python base_eventhandler() {
	from bb.event import getName

	name = getName(e)

	if name.startswith("BuildStarted"):
		e.data.setVar( 'BB_VERSION', bb.__version__)
		statusvars = ['BB_VERSION', 'TARGET_ARCH', 'TARGET_OS', 'MACHINE', 'DISTRO', 'DISTRO_VERSION','TUNE_FEATURES', 'TARGET_FPU']
		statuslines = ["%-17s = \"%s\"" % (i, e.data.getVar(i, 1) or '') for i in statusvars]

		statuslines += get_layers_branch_rev(e.data)
		statusmsg = "\nOE Build Configuration:\n%s\n" % '\n'.join(statuslines)
		bb.plain(statusmsg)

		needed_vars = [ "TARGET_ARCH", "TARGET_OS" ]
		pesteruser = []
		for v in needed_vars:
			val = e.data.getVar(v, 1)
			if not val or val == 'INVALID':
				pesteruser.append(v)
		if pesteruser:
			bb.fatal('The following variable(s) were not set: %s\nPlease set them directly, or choose a MACHINE or DISTRO that sets them.' % ', '.join(pesteruser))

        if name == "ConfigParsed":
                generate_git_config(e)
                pkgarch_mapping(e.data)
                preferred_ml_updates(e.data)
}

addtask configure after do_patch
do_configure[dirs] = "${CCACHE_DIR} ${S} ${B}"
do_configure[deptask] = "do_populate_sysroot"
base_do_configure() {
	:
}

addtask compile after do_configure
do_compile[dirs] = "${S} ${B}"
base_do_compile() {
	if [ -e Makefile -o -e makefile ]; then
		oe_runmake || die "make failed"
	else
		bbnote "nothing to compile"
	fi
}

addtask install after do_compile
do_install[dirs] = "${D} ${S} ${B}"
# Remove and re-create ${D} so that is it guaranteed to be empty
do_install[cleandirs] = "${D}"

base_do_install() {
	:
}

base_do_package() {
	:
}

addtask build after do_populate_sysroot
do_build = ""
do_build[func] = "1"
do_build[noexec] = "1"
do_build[recrdeptask] += "do_deploy"
do_build () {
	:
}

python () {
    import exceptions, string, re

    # Handle PACKAGECONFIG
    #
    # These take the form:
    #
    # PACKAGECONFIG ?? = "<default options>"
    # PACKAGECONFIG[foo] = "--enable-foo,--disable-foo,foo_depends,foo_runtime_depends"
    pkgconfig = (d.getVar('PACKAGECONFIG', True) or "").split()
    if pkgconfig:
        def appendVar(varname, appends):
            if not appends:
                return
            varname = bb.data.expand(varname, d)
            content = d.getVar(varname, False) or ""
            content = content + " " + " ".join(appends)
            d.setVar(varname, content)

        extradeps = []
        extrardeps = []
        extraconf = []
        for flag, flagval in (d.getVarFlags("PACKAGECONFIG") or {}).items():
            if flag == "defaultval":
                continue
            items = flagval.split(",")
            if len(items) == 3:
                enable, disable, depend = items
                rdepend = ""
            elif len(items) == 4:
                enable, disable, depend, rdepend = items
            if flag in pkgconfig:
                extradeps.append(depend)
                extrardeps.append(rdepend)
                extraconf.append(enable)
            else:
                extraconf.append(disable)
        appendVar('DEPENDS', extradeps)
        appendVar('RDEPENDS_${PN}', extrardeps)
        appendVar('EXTRA_OECONF', extraconf)

    # If PRINC is set, try and increase the PR value by the amount specified
    princ = d.getVar('PRINC', True)
    if princ and princ != "0":
        pr = d.getVar('PR', True)
        pr_prefix = re.search("\D+",pr)
        prval = re.search("\d+",pr)
        if pr_prefix is None or prval is None:
            bb.error("Unable to analyse format of PR variable: %s" % pr)
        nval = int(prval.group(0)) + int(princ)
        pr = pr_prefix.group(0) + str(nval) + pr[prval.end():]
        d.setVar('PR', pr)

    pn = d.getVar('PN', 1)
    license = d.getVar('LICENSE', True)
    if license == "INVALID":
        bb.fatal('This recipe does not have the LICENSE field set (%s)' % pn)

    commercial_license = " %s " % d.getVar('COMMERCIAL_LICENSE', 1)
    import re
    pnr = "[ \t]%s[ \t]" % pn.replace('+', "\+")
    if commercial_license and re.search(pnr, commercial_license):
        bb.debug(1, "Skipping %s because it's commercially licensed" % pn)
        raise bb.parse.SkipPackage("because it may require a commercial license to ship in a product (listed in COMMERCIAL_LICENSE)")

    # If we're building a target package we need to use fakeroot (pseudo)
    # in order to capture permissions, owners, groups and special files
    if not bb.data.inherits_class('native', d) and not bb.data.inherits_class('cross', d):
        d.setVarFlag('do_configure', 'umask', 022)
        d.setVarFlag('do_compile', 'umask', 022)
        deps = (d.getVarFlag('do_install', 'depends') or "").split()
        deps.append('virtual/fakeroot-native:do_populate_sysroot')
        d.setVarFlag('do_install', 'depends', " ".join(deps))
        d.setVarFlag('do_install', 'fakeroot', 1)
        d.setVarFlag('do_install', 'umask', 022)
        deps = (d.getVarFlag('do_package', 'depends') or "").split()
        deps.append('virtual/fakeroot-native:do_populate_sysroot')
        d.setVarFlag('do_package', 'depends', " ".join(deps))
        d.setVarFlag('do_package', 'fakeroot', 1)
        d.setVarFlag('do_package', 'umask', 022)
        d.setVarFlag('do_package_setscene', 'fakeroot', 1)
    source_mirror_fetch = d.getVar('SOURCE_MIRROR_FETCH', 0)
    if not source_mirror_fetch:
        need_host = d.getVar('COMPATIBLE_HOST', 1)
        if need_host:
            import re
            this_host = d.getVar('HOST_SYS', 1)
            if not re.match(need_host, this_host):
                raise bb.parse.SkipPackage("incompatible with host %s" % this_host)

        need_machine = d.getVar('COMPATIBLE_MACHINE', 1)
        if need_machine:
            import re
            this_machine = d.getVar('MACHINE', 1)
            if this_machine and not re.match(need_machine, this_machine):
                this_soc_family = d.getVar('SOC_FAMILY', 1)
                if (this_soc_family and not re.match(need_machine, this_soc_family)) or not this_soc_family:
                    raise bb.parse.SkipPackage("incompatible with machine %s" % this_machine)


        dont_want_license = d.getVar('INCOMPATIBLE_LICENSE', 1)
        if dont_want_license and not pn.endswith("-native") and not pn.endswith("-cross") and not pn.endswith("-cross-initial") and not pn.endswith("-cross-intermediate") and not pn.endswith("-crosssdk-intermediate") and not pn.endswith("-crosssdk") and not pn.endswith("-crosssdk-initial"):
            hosttools_whitelist = (d.getVar('HOSTTOOLS_WHITELIST_%s' % dont_want_license, 1) or "").split()
            lgplv2_whitelist = (d.getVar('LGPLv2_WHITELIST_%s' % dont_want_license, 1) or "").split()
            dont_want_whitelist = (d.getVar('WHITELIST_%s' % dont_want_license, 1) or "").split()
            if pn not in hosttools_whitelist and pn not in lgplv2_whitelist and pn not in dont_want_whitelist:

                import re
                this_license = d.getVar('LICENSE', 1)
                if this_license and re.search(dont_want_license, this_license):
                    bb.note("SKIPPING %s because it's %s" % (pn, this_license))
                    raise bb.parse.SkipPackage("incompatible with license %s" % this_license)

    srcuri = d.getVar('SRC_URI', 1)
    # Svn packages should DEPEND on subversion-native
    if "svn://" in srcuri:
        depends = d.getVarFlag('do_fetch', 'depends') or ""
        depends = depends + " subversion-native:do_populate_sysroot"
        d.setVarFlag('do_fetch', 'depends', depends)

    # Git packages should DEPEND on git-native
    if "git://" in srcuri:
        depends = d.getVarFlag('do_fetch', 'depends') or ""
        depends = depends + " git-native:do_populate_sysroot"
        d.setVarFlag('do_fetch', 'depends', depends)

    # Mercurial packages should DEPEND on mercurial-native
    elif "hg://" in srcuri:
        depends = d.getVarFlag('do_fetch', 'depends') or ""
        depends = depends + " mercurial-native:do_populate_sysroot"
        d.setVarFlag('do_fetch', 'depends', depends)

    # OSC packages should DEPEND on osc-native
    elif "osc://" in srcuri:
        depends = d.getVarFlag('do_fetch', 'depends') or ""
        depends = depends + " osc-native:do_populate_sysroot"
        d.setVarFlag('do_fetch', 'depends', depends)

    # *.xz should depends on xz-native for unpacking
    # Not endswith because of "*.patch.xz;patch=1". Need bb.decodeurl in future
    if '.xz' in srcuri:
        depends = d.getVarFlag('do_unpack', 'depends') or ""
        depends = depends + " xz-native:do_populate_sysroot"
        d.setVarFlag('do_unpack', 'depends', depends)

    # unzip-native should already be staged before unpacking ZIP recipes
    if ".zip" in srcuri:
        depends = d.getVarFlag('do_unpack', 'depends') or ""
        depends = depends + " unzip-native:do_populate_sysroot"
        d.setVarFlag('do_unpack', 'depends', depends)

    # 'multimachine' handling
    mach_arch = d.getVar('MACHINE_ARCH', 1)
    pkg_arch = d.getVar('PACKAGE_ARCH', 1)

    if (pkg_arch == mach_arch):
        # Already machine specific - nothing further to do
        return

    #
    # We always try to scan SRC_URI for urls with machine overrides
    # unless the package sets SRC_URI_OVERRIDES_PACKAGE_ARCH=0
    #
    override = d.getVar('SRC_URI_OVERRIDES_PACKAGE_ARCH', 1)
    if override != '0':
        paths = []
        for p in [ "${PF}", "${P}", "${PN}", "files", "" ]:
            path = bb.data.expand(os.path.join("${FILE_DIRNAME}", p, "${MACHINE}"), d)
            if os.path.isdir(path):
                paths.append(path)
        if len(paths) != 0:
            for s in srcuri.split():
                if not s.startswith("file://"):
                    continue
                fetcher = bb.fetch2.Fetch([s], d)
                local = fetcher.localpath(s)
                for mp in paths:
                    if local.startswith(mp):
                        #bb.note("overriding PACKAGE_ARCH from %s to %s" % (pkg_arch, mach_arch))
                        d.setVar('PACKAGE_ARCH', "${MACHINE_ARCH}")
                        return

    packages = d.getVar('PACKAGES', 1).split()
    for pkg in packages:
        pkgarch = d.getVar("PACKAGE_ARCH_%s" % pkg, 1)

        # We could look for != PACKAGE_ARCH here but how to choose
        # if multiple differences are present?
        # Look through PACKAGE_ARCHS for the priority order?
        if pkgarch and pkgarch == mach_arch:
            d.setVar('PACKAGE_ARCH', "${MACHINE_ARCH}")
            bb.warn("Recipe %s is marked as only being architecture specific but seems to have machine specific packages?! The recipe may as well mark itself as machine specific directly." % d.getVar("PN", True))
}

addtask cleansstate after do_clean
python do_cleansstate() {
        sstate_clean_cachefiles(d)
}

addtask cleanall after do_cleansstate
python do_cleanall() {
        src_uri = (d.getVar('SRC_URI', True) or "").split()
        if len(src_uri) == 0:
            return

	localdata = bb.data.createCopy(d)
	bb.data.update_data(localdata)

        try:
            fetcher = bb.fetch2.Fetch(src_uri, localdata)
            fetcher.clean()
        except bb.fetch2.BBFetchException, e:
            raise bb.build.FuncFailed(e)
}
do_cleanall[nostamp] = "1"


EXPORT_FUNCTIONS do_fetch do_unpack do_configure do_compile do_install do_package
