# Populates LICENSE_DIRECTORY as set in distro config with the license files as set by
# LIC_FILES_CHKSUM.
# TODO:
# - There is a real issue revolving around license naming standards.

LICENSE_DIRECTORY ??= "${DEPLOY_DIR}/licenses"
LICSSTATEDIR = "${WORKDIR}/license-destdir/"

# Create extra package with license texts and add it to RRECOMMENDS_${PN}
LICENSE_CREATE_PACKAGE[type] = "boolean"
LICENSE_CREATE_PACKAGE ??= "0"
LICENSE_PACKAGE_SUFFIX ??= "-lic"
LICENSE_FILES_DIRECTORY ??= "${datadir}/licenses/"

addtask populate_lic after do_patch before do_build
do_populate_lic[dirs] = "${LICSSTATEDIR}/${PN}"
do_populate_lic[cleandirs] = "${LICSSTATEDIR}"

python write_package_manifest() {
    # Get list of installed packages
    license_image_dir = d.expand('${LICENSE_DIRECTORY}/${IMAGE_NAME}')
    bb.utils.mkdirhier(license_image_dir)
    from oe.rootfs import image_list_installed_packages
    open(os.path.join(license_image_dir, 'package.manifest'),
        'w+').write(image_list_installed_packages(d))
}

license_create_manifest() {
        # Test if BUILD_IMAGES_FROM_FEEDS is defined in env
        if [ -n "${BUILD_IMAGES_FROM_FEEDS}" ]; then
          exit 0
        fi

	INSTALLED_PKGS=`cat ${LICENSE_DIRECTORY}/${IMAGE_NAME}/package.manifest`
	LICENSE_MANIFEST="${LICENSE_DIRECTORY}/${IMAGE_NAME}/license.manifest"
	# remove existing license.manifest file
	if [ -f ${LICENSE_MANIFEST} ]; then
		rm ${LICENSE_MANIFEST}
	fi
	touch ${LICENSE_MANIFEST}
	for pkg in ${INSTALLED_PKGS}; do
		filename=`ls ${PKGDATA_DIR}/runtime-reverse/${pkg}| head -1`
		pkged_pn="$(sed -n 's/^PN: //p' ${filename})"

		# check to see if the package name exists in the manifest. if so, bail.
		if grep -q "^PACKAGE NAME: ${pkg}" ${LICENSE_MANIFEST}; then
			continue
		fi

		pkged_pv="$(sed -n 's/^PV: //p' ${filename})"
		pkged_name="$(basename $(readlink ${filename}))"
		pkged_lic="$(sed -n "/^LICENSE_${pkged_name}: /{ s/^LICENSE_${pkged_name}: //; p }" ${filename})"
		if [ -z "${pkged_lic}" ]; then
			# fallback checking value of LICENSE
			pkged_lic="$(sed -n "/^LICENSE: /{ s/^LICENSE: //; p }" ${filename})"
		fi

		echo "PACKAGE NAME:" ${pkg} >> ${LICENSE_MANIFEST}
		echo "PACKAGE VERSION:" ${pkged_pv} >> ${LICENSE_MANIFEST}
		echo "RECIPE NAME:" ${pkged_pn} >> ${LICENSE_MANIFEST}
		echo "LICENSE:" ${pkged_lic} >> ${LICENSE_MANIFEST}
		echo "" >> ${LICENSE_MANIFEST}

		lics="$(echo ${pkged_lic} | sed "s/[|&()*]/ /g" | sed "s/  */ /g" )"
		for lic in ${lics}; do
			# to reference a license file trim trailing + symbol
			if ! [ -e "${LICENSE_DIRECTORY}/${pkged_pn}/generic_${lic%+}" ]; then
				bbwarn "The license listed ${lic} was not in the licenses collected for ${pkged_pn}"
			fi
		done
	done

	# Two options here:
	# - Just copy the manifest
	# - Copy the manifest and the license directories
	# With both options set we see a .5 M increase in core-image-minimal
	if [ "${COPY_LIC_MANIFEST}" = "1" ]; then
		mkdir -p ${IMAGE_ROOTFS}/usr/share/common-licenses/
		cp ${LICENSE_MANIFEST} ${IMAGE_ROOTFS}/usr/share/common-licenses/license.manifest
		if [ "${COPY_LIC_DIRS}" = "1" ]; then
			for pkg in ${INSTALLED_PKGS}; do
				mkdir -p ${IMAGE_ROOTFS}/usr/share/common-licenses/${pkg}
				pkged_pn="$(oe-pkgdata-util lookup-recipe ${PKGDATA_DIR} ${pkg})"
				for lic in `ls ${LICENSE_DIRECTORY}/${pkged_pn}`; do
					# Really don't need to copy the generics as they're 
					# represented in the manifest and in the actual pkg licenses
					# Doing so would make your image quite a bit larger
					if [ "${lic#generic_}" = "${lic}" ]; then
						cp ${LICENSE_DIRECTORY}/${pkged_pn}/${lic} ${IMAGE_ROOTFS}/usr/share/common-licenses/${pkg}/${lic}
					else
						if [ ! -f ${IMAGE_ROOTFS}/usr/share/common-licenses/${lic} ]; then
							cp ${LICENSE_DIRECTORY}/${pkged_pn}/${lic} ${IMAGE_ROOTFS}/usr/share/common-licenses/
						fi
						ln -sf ../${lic} ${IMAGE_ROOTFS}/usr/share/common-licenses/${pkg}/${lic}
					fi
				done
			done
		fi
	fi

}

python do_populate_lic() {
    """
    Populate LICENSE_DIRECTORY with licenses.
    """
    lic_files_paths = find_license_files(d)

    # The base directory we wrangle licenses to
    destdir = os.path.join(d.getVar('LICSSTATEDIR', True), d.getVar('PN', True))
    copy_license_files(lic_files_paths, destdir)
}

# it would be better to copy them in do_install_append, but find_license_filesa is python
python perform_packagecopy_prepend () {
    enabled = oe.data.typed_value('LICENSE_CREATE_PACKAGE', d)
    if d.getVar('CLASSOVERRIDE', True) == 'class-target' and enabled:
        lic_files_paths = find_license_files(d)

        # LICENSE_FILES_DIRECTORY starts with '/' so os.path.join cannot be used to join D and LICENSE_FILES_DIRECTORY
        destdir = d.getVar('D', True) + os.path.join(d.getVar('LICENSE_FILES_DIRECTORY', True), d.getVar('PN', True))
        copy_license_files(lic_files_paths, destdir)
        add_package_and_files(d)
}

def add_package_and_files(d):
    packages = d.getVar('PACKAGES', True)
    files = d.getVar('LICENSE_FILES_DIRECTORY', True)
    pn = d.getVar('PN', True)
    pn_lic = "%s%s" % (pn, d.getVar('LICENSE_PACKAGE_SUFFIX'))
    if pn_lic in packages:
        bb.warn("%s package already existed in %s." % (pn_lic, pn))
    else:
        # first in PACKAGES to be sure that nothing else gets LICENSE_FILES_DIRECTORY
        d.setVar('PACKAGES', "%s %s" % (pn_lic, packages))
        d.setVar('FILES_' + pn_lic, files)
        rrecommends_pn = d.getVar('RRECOMMENDS_' + pn, True)
        if rrecommends_pn:
            d.setVar('RRECOMMENDS_' + pn, "%s %s" % (pn_lic, rrecommends_pn))
        else:
            d.setVar('RRECOMMENDS_' + pn, "%s" % (pn_lic))

def copy_license_files(lic_files_paths, destdir):
    import shutil

    bb.utils.mkdirhier(destdir)
    for (basename, path) in lic_files_paths:
        try:
            src = path
            dst = os.path.join(destdir, basename)
            if os.path.exists(dst):
                os.remove(dst)
            if (os.stat(src).st_dev == os.stat(destdir).st_dev):
                os.link(src, dst)
            else:
                shutil.copyfile(src, dst)
        except Exception as e:
            bb.warn("Could not copy license file %s: %s" % (basename, e))

def find_license_files(d):
    """
    Creates list of files used in LIC_FILES_CHKSUM and generic LICENSE files.
    """
    import shutil
    import oe.license

    pn = d.getVar('PN', True)
    for package in d.getVar('PACKAGES', True):
        if d.getVar('LICENSE_' + package, True):
            license_types = license_types + ' & ' + \
                            d.getVar('LICENSE_' + package, True)

    #If we get here with no license types, then that means we have a recipe 
    #level license. If so, we grab only those.
    try:
        license_types
    except NameError:        
        # All the license types at the recipe level
        license_types = d.getVar('LICENSE', True)
 
    # All the license files for the package
    lic_files = d.getVar('LIC_FILES_CHKSUM', True)
    pn = d.getVar('PN', True)
    # The license files are located in S/LIC_FILE_CHECKSUM.
    srcdir = d.getVar('S', True)
    # Directory we store the generic licenses as set in the distro configuration
    generic_directory = d.getVar('COMMON_LICENSE_DIR', True)
    # List of basename, path tuples
    lic_files_paths = []
    license_source_dirs = []
    license_source_dirs.append(generic_directory)
    try:
        additional_lic_dirs = d.getVar('LICENSE_PATH', True).split()
        for lic_dir in additional_lic_dirs:
            license_source_dirs.append(lic_dir)
    except:
        pass

    class FindVisitor(oe.license.LicenseVisitor):
        def visit_Str(self, node):
            #
            # Until I figure out what to do with
            # the two modifiers I support (or greater = +
            # and "with exceptions" being *
            # we'll just strip out the modifier and put
            # the base license.
            find_license(node.s.replace("+", "").replace("*", ""))
            self.generic_visit(node)

    def find_license(license_type):
        try:
            bb.utils.mkdirhier(gen_lic_dest)
        except:
            pass
        spdx_generic = None
        license_source = None
        # If the generic does not exist we need to check to see if there is an SPDX mapping to it
        for lic_dir in license_source_dirs:
            if not os.path.isfile(os.path.join(lic_dir, license_type)):
                if d.getVarFlag('SPDXLICENSEMAP', license_type) != None:
                    # Great, there is an SPDXLICENSEMAP. We can copy!
                    bb.debug(1, "We need to use a SPDXLICENSEMAP for %s" % (license_type))
                    spdx_generic = d.getVarFlag('SPDXLICENSEMAP', license_type)
                    license_source = lic_dir
                    break
            elif os.path.isfile(os.path.join(lic_dir, license_type)):
                spdx_generic = license_type
                license_source = lic_dir
                break

        if spdx_generic and license_source:
            # we really should copy to generic_ + spdx_generic, however, that ends up messing the manifest
            # audit up. This should be fixed in emit_pkgdata (or, we actually got and fix all the recipes)

            lic_files_paths.append(("generic_" + license_type, os.path.join(license_source, spdx_generic)))
        else:
            # And here is where we warn people that their licenses are lousy
            bb.warn("%s: No generic license file exists for: %s in any provider" % (pn, license_type))
            pass

    if not generic_directory:
        raise bb.build.FuncFailed("COMMON_LICENSE_DIR is unset. Please set this in your distro config")

    if not lic_files:
        # No recipe should have an invalid license file. This is checked else
        # where, but let's be pedantic
        bb.note(pn + ": Recipe file does not have license file information.")
        return lic_files_paths

    for url in lic_files.split():
        try:
            (type, host, path, user, pswd, parm) = bb.fetch.decodeurl(url)
        except bb.fetch.MalformedUrl:
            raise bb.build.FuncFailed("%s: LIC_FILES_CHKSUM contains an invalid URL:  %s" % (d.getVar('PF', True), url))
        # We want the license filename and path
        srclicfile = os.path.join(srcdir, path)
        lic_files_paths.append((os.path.basename(path), srclicfile))

    v = FindVisitor()
    try:
        v.visit_string(license_types)
    except oe.license.InvalidLicense as exc:
        bb.fatal('%s: %s' % (d.getVar('PF', True), exc))
    except SyntaxError:
        bb.warn("%s: Failed to parse it's LICENSE field." % (d.getVar('PF', True)))

    return lic_files_paths

def return_spdx(d, license):
    """
    This function returns the spdx mapping of a license if it exists.
     """
    return d.getVarFlag('SPDXLICENSEMAP', license, True)

def canonical_license(d, license):
    """
    Return the canonical (SPDX) form of the license if available (so GPLv3
    becomes GPL-3.0), for the license named 'X+', return canonical form of
    'X' if availabel and the tailing '+' (so GPLv3+ becomes GPL-3.0+), 
    or the passed license if there is no canonical form.
    """
    lic = d.getVarFlag('SPDXLICENSEMAP', license, True) or ""
    if not lic and license.endswith('+'):
        lic = d.getVarFlag('SPDXLICENSEMAP', license.rstrip('+'), True)
        if lic:
            lic += '+'
    return lic or license

def incompatible_license(d, dont_want_licenses, package=None):
    """
    This function checks if a recipe has only incompatible licenses. It also
    take into consideration 'or' operand.  dont_want_licenses should be passed
    as canonical (SPDX) names.
    """
    import re
    import oe.license
    from fnmatch import fnmatchcase as fnmatch
    license = d.getVar("LICENSE_%s" % package, True) if package else None
    if not license:
        license = d.getVar('LICENSE', True)

    def license_ok(license):
        for dwl in dont_want_licenses:
            # If you want to exclude license named generically 'X', we
            # surely want to exclude 'X+' as well.  In consequence, we
            # will exclude a trailing '+' character from LICENSE in
            # case INCOMPATIBLE_LICENSE is not a 'X+' license.
            lic = license
            if not re.search('\+$', dwl):
                lic = re.sub('\+', '', license)
            if fnmatch(lic, dwl):
                return False
        return True

    # Handles an "or" or two license sets provided by
    # flattened_licenses(), pick one that works if possible.
    def choose_lic_set(a, b):
        return a if all(license_ok(canonical_license(d, lic)) for lic in a) \
                else b

    try:
        licenses = oe.license.flattened_licenses(license, choose_lic_set)
    except oe.license.LicenseError as exc:
        bb.fatal('%s: %s' % (d.getVar('P', True), exc))
    return any(not license_ok(canonical_license(d, l)) for l in licenses)

def check_license_flags(d):
    """
    This function checks if a recipe has any LICENSE_FLAGS that
    aren't whitelisted.

    If it does, it returns the first LICENSE_FLAGS item missing from the
    whitelist, or all of the LICENSE_FLAGS if there is no whitelist.

    If everything is is properly whitelisted, it returns None.
    """

    def license_flag_matches(flag, whitelist, pn):
        """
        Return True if flag matches something in whitelist, None if not.

        Before we test a flag against the whitelist, we append _${PN}
        to it.  We then try to match that string against the
        whitelist.  This covers the normal case, where we expect
        LICENSE_FLAGS to be a simple string like 'commercial', which
        the user typically matches exactly in the whitelist by
        explicitly appending the package name e.g 'commercial_foo'.
        If we fail the match however, we then split the flag across
        '_' and append each fragment and test until we either match or
        run out of fragments.
        """
        flag_pn = ("%s_%s" % (flag, pn))
        for candidate in whitelist:
            if flag_pn == candidate:
                    return True

        flag_cur = ""
        flagments = flag_pn.split("_")
        flagments.pop() # we've already tested the full string
        for flagment in flagments:
            if flag_cur:
                flag_cur += "_"
            flag_cur += flagment
            for candidate in whitelist:
                if flag_cur == candidate:
                    return True
        return False

    def all_license_flags_match(license_flags, whitelist):
        """ Return first unmatched flag, None if all flags match """
        pn = d.getVar('PN', True)
        split_whitelist = whitelist.split()
        for flag in license_flags.split():
            if not license_flag_matches(flag, split_whitelist, pn):
                return flag
        return None

    license_flags = d.getVar('LICENSE_FLAGS', True)
    if license_flags:
        whitelist = d.getVar('LICENSE_FLAGS_WHITELIST', True)
        if not whitelist:
            return license_flags
        unmatched_flag = all_license_flags_match(license_flags, whitelist)
        if unmatched_flag:
            return unmatched_flag
    return None

SSTATETASKS += "do_populate_lic"
do_populate_lic[sstate-inputdirs] = "${LICSSTATEDIR}"
do_populate_lic[sstate-outputdirs] = "${LICENSE_DIRECTORY}/"

ROOTFS_POSTPROCESS_COMMAND_prepend = "write_package_manifest; license_create_manifest; "

do_populate_lic_setscene[dirs] = "${LICSSTATEDIR}/${PN}"
do_populate_lic_setscene[cleandirs] = "${LICSSTATEDIR}"
python do_populate_lic_setscene () {
    sstate_setscene(d)
}
addtask do_populate_lic_setscene
