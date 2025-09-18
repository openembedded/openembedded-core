#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

# The list of packages that should have systemd packaging scripts added.  For
# each entry, optionally have a SYSTEMD_SERVICE:[package] that lists the service
# files in this package.  If this variable isn't set, [package].service is used.
SYSTEMD_PACKAGES ?= "${PN}"
SYSTEMD_PACKAGES:class-native ?= ""
SYSTEMD_PACKAGES:class-nativesdk ?= ""

# Whether to enable or disable the services on installation.
SYSTEMD_AUTO_ENABLE ??= "enable"

# This class will be included in any recipe that supports systemd init scripts,
# even if systemd is not in DISTRO_FEATURES.  As such don't make any changes
# directly but check the DISTRO_FEATURES first.
python __anonymous() {
    # If the distro features have systemd but not sysvinit, inhibit update-rcd
    # from doing any work so that pure-systemd images don't have redundant init
    # files.
    if bb.utils.contains('DISTRO_FEATURES', 'systemd', True, False, d):
        d.appendVar("DEPENDS", " systemd-systemctl-native")
        d.appendVar("PACKAGE_WRITE_DEPS", " systemd-systemctl-native")
        if not bb.utils.contains('DISTRO_FEATURES', 'sysvinit', True, False, d):
            d.setVar("INHIBIT_UPDATERCD_BBCLASS", "1")
}

systemd_postinst() {
if systemctl >/dev/null 2>/dev/null; then
	OPTS=""

	if [ -n "$D" ]; then
		OPTS="--root=$D"
	fi

	if [ "${SYSTEMD_AUTO_ENABLE}" = "enable" ]; then
		for service in ${@systemd_filter_services("${SYSTEMD_SERVICE_ESCAPED}", False, d)}; do
			systemctl ${OPTS} enable "$service"
		done

		for service in ${@systemd_filter_services("${SYSTEMD_SERVICE_ESCAPED}", True, d)}; do
			systemctl --global ${OPTS} enable "$service"
		done
	fi

	if [ -z "$D" ]; then
		# Reload only system service manager
		# --global for daemon-reload is not supported: https://github.com/systemd/systemd/issues/19284
		systemctl daemon-reload
		[ -n "${@systemd_filter_services("${SYSTEMD_SERVICE_ESCAPED}", False, d)}" ] && \
			systemctl preset ${@systemd_filter_services("${SYSTEMD_SERVICE_ESCAPED}", False, d)}

		[ -n "${@systemd_filter_services("${SYSTEMD_SERVICE_ESCAPED}", True, d)}" ] && \
			systemctl --global preset ${@systemd_filter_services("${SYSTEMD_SERVICE_ESCAPED}", True, d)}

		if [ "${SYSTEMD_AUTO_ENABLE}" = "enable" ]; then
			# --global flag for restart is not supported by systemd (see above)
			[ -n "${@systemd_filter_services("${SYSTEMD_SERVICE_ESCAPED}", False, d)}" ] && \
				systemctl --no-block restart ${@systemd_filter_services("${SYSTEMD_SERVICE_ESCAPED}", False, d)}
		fi
	fi
fi
}

systemd_prerm() {
if systemctl >/dev/null 2>/dev/null; then
	if [ -z "$D" ]; then
		if [ -n "${@systemd_filter_services("${SYSTEMD_SERVICE_ESCAPED}", False, d)}" ]; then
			systemctl stop ${@systemd_filter_services("${SYSTEMD_SERVICE_ESCAPED}", False, d)}
			systemctl disable ${@systemd_filter_services("${SYSTEMD_SERVICE_ESCAPED}", False, d)}
		fi

		# same as above, --global flag is not supported for stop so do disable only
		[ -n "${@systemd_filter_services("${SYSTEMD_SERVICE_ESCAPED}", True, d)}" ] && \
			systemctl --global disable ${@systemd_filter_services("${SYSTEMD_SERVICE_ESCAPED}", True, d)}
	fi
fi
}


systemd_populate_packages[vardeps] += "systemd_prerm systemd_postinst"
systemd_populate_packages[vardepsexclude] += "OVERRIDES"


def systemd_service_path(service, searchpaths, d):
    path_found = ''

    # Deal with adding, for example, 'ifplugd@eth0.service' from
    # 'ifplugd@.service'
    base = None
    at = service.find('@')
    if at != -1:
        ext = service.rfind('.')
        base = service[:at] + '@' + service[ext:]

    for path in searchpaths:
        if os.path.lexists(oe.path.join(d.getVar("D"), path, service)):
            path_found = path
            break
        elif base is not None:
            if os.path.exists(oe.path.join(d.getVar("D"), path, base)):
                path_found = path
                break

    return path_found, base

def systemd_service_searchpaths(user, d):
    if user:
        return [
            oe.path.join(d.getVar("sysconfdir"), "systemd", "user"),
            d.getVar("systemd_user_unitdir"),
        ]
    else:
        return [
            oe.path.join(d.getVar("sysconfdir"), "systemd", "system"),
            d.getVar("systemd_system_unitdir"),
        ]

def systemd_service_exists(service, user, d):
    searchpaths = systemd_service_searchpaths(user, d)
    path, _ = systemd_service_path(service, searchpaths, d)

    return path != ''

def systemd_filter_services(services, user, d):
    return ' '.join(service for service in services.split() if systemd_service_exists(service, user, d))

python systemd_populate_packages() {
    import re
    import shlex

    if not bb.utils.contains('DISTRO_FEATURES', 'systemd', True, False, d):
        return

    def get_package_var(d, var, pkg):
        val = (d.getVar('%s:%s' % (var, pkg)) or "").strip()
        if val == "":
            val = (d.getVar(var) or "").strip()
        return val

    # Check if systemd-packages already included in PACKAGES
    def systemd_check_package(pkg_systemd):
        packages = d.getVar('PACKAGES')
        if not pkg_systemd in packages.split():
            bb.error('%s is marked for packaging systemd scripts, but it does not appear in package list, please add it to PACKAGES or adjust SYSTEMD_PACKAGES accordingly' % pkg_systemd)


    def systemd_generate_package_scripts(pkg):
        bb.debug(1, 'adding systemd calls to postinst/postrm for %s' % pkg)

        paths_escaped = ' '.join(shlex.quote(s) for s in d.getVar('SYSTEMD_SERVICE:' + pkg).split())
        d.setVar('SYSTEMD_SERVICE_ESCAPED:' + pkg, paths_escaped)

        # Add pkg to the overrides so that it finds the SYSTEMD_SERVICE:pkg
        # variable.
        localdata = d.createCopy()
        localdata.prependVar("OVERRIDES", pkg + ":")

        postinst = d.getVar('pkg_postinst:%s' % pkg)
        if not postinst:
            postinst = '#!/bin/sh\n'
        postinst += localdata.getVar('systemd_postinst')
        d.setVar('pkg_postinst:%s' % pkg, postinst)

        prerm = d.getVar('pkg_prerm:%s' % pkg)
        if not prerm:
            prerm = '#!/bin/sh\n'
        prerm += localdata.getVar('systemd_prerm')
        d.setVar('pkg_prerm:%s' % pkg, prerm)


    # Add files to FILES:*-systemd if existent and not already done
    def systemd_append_file(pkg_systemd, file_append):
        appended = False
        if os.path.exists(oe.path.join(d.getVar("D"), file_append)):
            var_name = "FILES:" + pkg_systemd
            files = d.getVar(var_name, False) or ""
            if file_append not in files.split():
                d.appendVar(var_name, " " + file_append)
                appended = True
        return appended

    # Add systemd files to FILES:*-systemd, parse for Also= and follow recursive
    def systemd_add_files_and_parse(pkg_systemd, path, service, keys):
        # avoid infinite recursion
        if systemd_append_file(pkg_systemd, oe.path.join(path, service)):
            fullpath = oe.path.join(d.getVar("D"), path, service)
            if service.find('.service') != -1:
                # for *.service add *@.service
                service_base = service.replace('.service', '')
                systemd_add_files_and_parse(pkg_systemd, path, service_base + '@.service', keys)
            if service.find('.socket') != -1:
                # for *.socket add *.service and *@.service
                service_base = service.replace('.socket', '')
                systemd_add_files_and_parse(pkg_systemd, path, service_base + '.service', keys)
                systemd_add_files_and_parse(pkg_systemd, path, service_base + '@.service', keys)
            for key in keys.split():
                # recurse all dependencies found in keys ('Also';'Conflicts';..) and add to files
                cmd = "grep %s %s | sed 's,%s=,,g' | tr ',' '\\n'" % (key, shlex.quote(fullpath), key)
                pipe = os.popen(cmd, 'r')
                line = pipe.readline()
                while line:
                    line = line.replace('\n', '')
                    systemd_add_files_and_parse(pkg_systemd, path, line, keys)
                    line = pipe.readline()
                pipe.close()

    # Check service-files and call systemd_add_files_and_parse for each entry
    def systemd_check_services():
        searchpaths = systemd_service_searchpaths(False, d)
        searchpaths.extend(systemd_service_searchpaths(True, d))

        systemd_packages = d.getVar('SYSTEMD_PACKAGES')

        keys = 'Also'
        # scan for all in SYSTEMD_SERVICE[]
        for pkg_systemd in systemd_packages.split():
            for service in get_package_var(d, 'SYSTEMD_SERVICE', pkg_systemd).split():
                path_found, base = systemd_service_path(service, searchpaths, d)

                if path_found != '':
                    systemd_add_files_and_parse(pkg_systemd, path_found, service, keys)
                else:
                    bb.fatal("Didn't find service unit '{0}', specified in SYSTEMD_SERVICE:{1}. {2}".format(
                        service, pkg_systemd, "Also looked for service unit '{0}'.".format(base) if base is not None else ""))

    def systemd_create_presets(pkg, action, user):
        # Check there is at least one service of given type (system/user), don't
        # create empty files.
        needs_preset = False
        for service in d.getVar('SYSTEMD_SERVICE:%s' % pkg).split():
            if systemd_service_exists(service, user, d):
                needs_preset = True
                break

        if not needs_preset:
            return

        prefix = "user" if user else "system"
        presetf = oe.path.join(d.getVar("PKGD"), d.getVar("systemd_unitdir"), "%s-preset/98-%s.preset" % (prefix, pkg))
        bb.utils.mkdirhier(os.path.dirname(presetf))
        with open(presetf, 'a') as fd:
            for service in d.getVar('SYSTEMD_SERVICE:%s' % pkg).split():
                if not systemd_service_exists(service, user, d):
                    continue
                fd.write("%s %s\n" % (action,service))
        d.appendVar("FILES:%s" % pkg, ' ' + oe.path.join(d.getVar("systemd_unitdir"), "%s-preset/98-%s.preset" % (prefix, pkg)))

    # Run all modifications once when creating package
    if os.path.exists(d.getVar("D")):
        for pkg in d.getVar('SYSTEMD_PACKAGES').split():
            systemd_check_package(pkg)
            if d.getVar('SYSTEMD_SERVICE:' + pkg):
                systemd_generate_package_scripts(pkg)
                action = get_package_var(d, 'SYSTEMD_AUTO_ENABLE', pkg)
                if action in ("enable", "disable"):
                    systemd_create_presets(pkg, action, False)
                    systemd_create_presets(pkg, action, True)
                elif action not in ("mask", "preset"):
                    bb.fatal("SYSTEMD_AUTO_ENABLE:%s '%s' is not 'enable', 'disable', 'mask' or 'preset'" % (pkg, action))
        systemd_check_services()
}

PACKAGESPLITFUNCS =+ "systemd_populate_packages"

python rm_systemd_unitdir (){
    import shutil
    if not bb.utils.contains('DISTRO_FEATURES', 'systemd', True, False, d):
        systemd_unitdir = oe.path.join(d.getVar("D"), d.getVar('systemd_unitdir'))
        if os.path.exists(systemd_unitdir):
            shutil.rmtree(systemd_unitdir)
        systemd_libdir = os.path.dirname(systemd_unitdir)
        if (os.path.exists(systemd_libdir) and not os.listdir(systemd_libdir)):
            os.rmdir(systemd_libdir)
}

python rm_sysvinit_initddir (){
    import shutil
    sysv_initddir = oe.path.join(d.getVar("D"), (d.getVar('INIT_D_DIR') or "/etc/init.d"))

    if bb.utils.contains('DISTRO_FEATURES', 'systemd', True, False, d) and \
        not bb.utils.contains('DISTRO_FEATURES', 'sysvinit', True, False, d) and \
        os.path.exists(sysv_initddir):
        systemd_system_unitdir = oe.path.join(d.getVar("D"), d.getVar('systemd_system_unitdir'))

        # If systemd_system_unitdir contains anything, delete sysv_initddir
        if (os.path.exists(systemd_system_unitdir) and os.listdir(systemd_system_unitdir)):
            shutil.rmtree(sysv_initddir)
}

do_install[postfuncs] += "${RMINITDIR} "
RMINITDIR:class-target = " rm_sysvinit_initddir rm_systemd_unitdir "
RMINITDIR:class-nativesdk = " rm_sysvinit_initddir rm_systemd_unitdir "
RMINITDIR = ""

