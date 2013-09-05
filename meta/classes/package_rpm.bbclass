inherit package

IMAGE_PKGTYPE ?= "rpm"

RPM="rpm"
RPMBUILD="rpmbuild"

PKGWRITEDIRRPM = "${WORKDIR}/deploy-rpms"
PKGWRITEDIRSRPM = "${DEPLOY_DIR}/sources/deploy-srpm"

# Maintaining the perfile dependencies has singificant overhead when writing the 
# packages. When set, this value merges them for efficiency.
MERGEPERFILEDEPS = "1"

#
# Update the packages indexes ${DEPLOY_DIR_RPM}
#
package_update_index_rpm () {
	if [ ! -z "${DEPLOY_KEEP_PACKAGES}" ]; then
		return
	fi

	sdk_archs=`echo "${SDK_PACKAGE_ARCHS}" | tr - _`

	target_archs=""
	for i in ${MULTILIB_PREFIX_LIST} ; do
		old_IFS="$IFS"
		IFS=":"
		set $i
		IFS="$old_IFS"
		shift # remove mlib
		while [ -n "$1" ]; do
			target_archs="$target_archs $1"
			shift
		done
	done

	target_archs=`echo "$target_archs" | tr - _`

	archs=`for arch in $target_archs $sdk_archs ; do
		echo $arch
	done | sort | uniq`

	found=0
	for arch in $archs; do
		if [ -d ${DEPLOY_DIR_RPM}/$arch ] ; then
			createrepo --update -q ${DEPLOY_DIR_RPM}/$arch
			found=1
		fi
	done
	if [ "$found" != "1" ]; then
		bbfatal "There are no packages in ${DEPLOY_DIR_RPM}!"
	fi
}

rpm_log_check() {
	target="$1"
	lf_path="$2"

	lf_txt="`cat $lf_path`"
	for keyword_die in "unpacking of archive failed" "Cannot find package" "exit 1" ERR Fail
	do
		if (echo "$lf_txt" | grep -v log_check | grep "$keyword_die") >/dev/null 2>&1
		then
			echo "log_check: There were error messages in the logfile"
			printf "log_check: Matched keyword: [$keyword_die]\n\n"
			echo "$lf_txt" | grep -v log_check | grep -C 5 -i "$keyword_die"
			echo ""
			do_exit=1
		fi
	done
	test "$do_exit" = 1 && exit 1
	true
}

# Translate the RPM/Smart format names to the OE multilib format names
# Input via stdin (only the first item per line is converted!)
# Output via stdout
translate_smart_to_oe() {
	arg1="$1"

	# Dump installed packages
	while read pkg arch other ; do
		found=0
		if [ -z "$pkg" ]; then
			continue
		fi
		new_pkg=$pkg
		fixed_arch=`echo "$arch" | tr _ -`
		for i in ${MULTILIB_PREFIX_LIST} ; do
			old_IFS="$IFS"
			IFS=":"
			set $i
			IFS="$old_IFS"
			mlib="$1"
			shift
			while [ -n "$1" ]; do
				cmp_arch=$1
				shift
				fixed_cmp_arch=`echo "$cmp_arch" | tr _ -`
				if [ "$fixed_arch" = "$fixed_cmp_arch" ]; then
					if [ "$mlib" = "default" ]; then
						new_pkg="$pkg"
						new_arch=$cmp_arch
					else
						new_pkg="$mlib-$pkg"
						# We need to strip off the ${mlib}_ prefix on the arch
						new_arch=${cmp_arch#${mlib}_}
					fi
					# Workaround for bug 3565
					# Simply look to see if we know of a package with that name, if not try again!
					filename=`ls ${TMPDIR}/pkgdata/*/runtime-reverse/$new_pkg 2>/dev/null | head -n 1`
					if [ -n "$filename" ] ; then
						found=1
						break
					fi
					# 'real' code
					# found=1
					# break
				fi
			done
			if [ "$found" = "1" ] && [ "$fixed_arch" = "$fixed_cmp_arch" ]; then
				break
			fi
		done

		#echo "$pkg -> $new_pkg" >&2
		if [ "$arg1" = "arch" ]; then
			echo $new_pkg $new_arch $other
		elif [ "$arg1" = "file" ]; then
			echo $new_pkg $other $new_arch
		else
			echo $new_pkg $other
		fi
	done
}		

# Translate the OE multilib format names to the RPM/Smart format names
# Input via arguments
# Ouput via pkgs_to_install
translate_oe_to_smart() {
	default_archs=""
	sdk_mode=""
	if [ "$1" = "--sdk" ]; then
		shift
		sdk_mode="true"
		# Need to reverse the order of the SDK_ARCHS highest -> lowest priority
		archs=`echo "${SDK_PACKAGE_ARCHS}" | tr - _`
		for arch in $archs ; do
		        default_archs="$arch $default_archs"
		done
	fi

	attemptonly="Error"
	if [ "$1" = "--attemptonly" ]; then
		attemptonly="Warning"
		shift
	fi

	# Dump a list of all available packages
	[ ! -e ${target_rootfs}/install/tmp/fullpkglist.query ] && smart --data-dir=${target_rootfs}/var/lib/smart query --output ${target_rootfs}/install/tmp/fullpkglist.query

	pkgs_to_install=""
	for pkg in "$@" ; do
		new_pkg="$pkg"
		if [ -z "$sdk_mode" ]; then
			for i in ${MULTILIB_PREFIX_LIST} ; do
				old_IFS="$IFS"
				IFS=":"
				set $i
				IFS="$old_IFS"
				mlib="$1"
				shift
				if [ "$mlib" = "default" ]; then
					if [ -z "$default_archs" ]; then
						default_archs=$@
					fi
					continue
				fi
				subst=${pkg#${mlib}-}
				if [ "$subst" != "$pkg" ]; then
					feeds=$@
					while [ -n "$1" ]; do
						arch="$1"
						arch=`echo "$arch" | tr - _`
						shift
						if grep -q '^'$subst'-[^-]*-[^-]*@'$arch'$' ${target_rootfs}/install/tmp/fullpkglist.query ; then
							new_pkg="$subst@$arch"
							# First found is best match
							break
						fi
					done
					if [ "$pkg" = "$new_pkg" ]; then
						# Failed to translate, package not found!
						echo "$attemptonly: $pkg not found in the $mlib feeds ($feeds)." >&2
						if [ "$attemptonly" = "Error" ]; then
							exit 1
						fi
						continue
					fi
				fi
			done
		fi
		# Apparently not a multilib package...
		if [ "$pkg" = "$new_pkg" ]; then
			default_archs_fixed=`echo "$default_archs" | tr - _`
			for arch in $default_archs_fixed ; do
				if grep -q '^'$pkg'-[^-]*-[^-]*@'$arch'$' ${target_rootfs}/install/tmp/fullpkglist.query ; then
					new_pkg="$pkg@$arch"
					# First found is best match
					break
				fi
			done
			if [ "$pkg" = "$new_pkg" ]; then
				# Failed to translate, package not found!
				echo "$attemptonly: $pkg not found in the base feeds ($default_archs)." >&2
				if [ "$attemptonly" = "Error" ]; then
					exit 1
				fi
				continue
			fi
		fi
		#echo "$pkg -> $new_pkg" >&2
		pkgs_to_install="${pkgs_to_install} ${new_pkg}"
	done
	export pkgs_to_install
}

package_write_smart_config() {
	# Write common configuration for host and target usage
	smart --data-dir=$1/var/lib/smart config --set rpm-nolinktos=1
	smart --data-dir=$1/var/lib/smart config --set rpm-noparentdirs=1
	for i in ${BAD_RECOMMENDATIONS}; do
		smart --data-dir=$1/var/lib/smart flag --set ignore-recommends $i
	done
}

#
# Install a bunch of packages using rpm.
# There are two solutions in an image's FRESH generation:
# 1) main package solution
# 2) complementary solution
#
# It is different when incremental image generation is enabled:
# 1) The incremental image generation takes action during the main package
#    installation, the previous installed complementary packages would
#    usually be removed here, and the new complementary ones would be
#    installed in the next step.
# 2) The complementary would always be installed since it is
#    generated based on the first step's image.
#
# the following shell variables needs to be set before calling this func:
# INSTALL_ROOTFS_RPM - install root dir
# INSTALL_PLATFORM_RPM - main platform
# INSTALL_PLATFORM_EXTRA_RPM - extra platform
# INSTALL_PACKAGES_RPM - packages to be installed
# INSTALL_PACKAGES_ATTEMPTONLY_RPM - packages attemped to be installed only
# INSTALL_PACKAGES_LINGUAS_RPM - additional packages for uclibc
# INSTALL_PROVIDENAME_RPM - content for provide name
# INSTALL_TASK_RPM - task name
# INSTALL_COMPLEMENTARY_RPM - 1 to enable complementary package install mode

package_install_internal_rpm () {

	local target_rootfs="$INSTALL_ROOTFS_RPM"
	local package_to_install="$INSTALL_PACKAGES_RPM"
	local package_attemptonly="$INSTALL_PACKAGES_ATTEMPTONLY_RPM"
	local package_linguas="$INSTALL_PACKAGES_LINGUAS_RPM"
	local providename="$INSTALL_PROVIDENAME_RPM"
	local task="$INSTALL_TASK_RPM"

	local sdk_mode=""
	if [ "$1" = "--sdk" ]; then
		sdk_mode="--sdk"
	fi

	# Configure internal RPM environment when using Smart
	export RPM_ETCRPM=${target_rootfs}/etc/rpm

	# Setup temporary directory -- install...
	rm -rf ${target_rootfs}/install
	mkdir -p ${target_rootfs}/install/tmp

	channel_priority=5
	if [ "${INSTALL_COMPLEMENTARY_RPM}" != "1" ] ; then
		# Setup base system configuration
		echo "Note: configuring RPM platform settings"
		mkdir -p ${target_rootfs}/etc/rpm/
		echo "$INSTALL_PLATFORM_RPM" > ${target_rootfs}/etc/rpm/platform

		if [ ! -z "$INSTALL_PLATFORM_EXTRA_RPM" ]; then
			for pt in $INSTALL_PLATFORM_EXTRA_RPM ; do
				channel_priority=$(expr $channel_priority + 5)
				case $pt in
					noarch-* | any-* | all-*)
						pt=$(echo $pt | sed "s,-linux.*$,-linux\.*,")
						;;
				esac
				echo "$pt" >> ${target_rootfs}/etc/rpm/platform
			done
		fi

		# Tell RPM that the "/" directory exist and is available
		echo "Note: configuring RPM system provides"
		mkdir -p ${target_rootfs}/etc/rpm/sysinfo
		echo "/" >${target_rootfs}/etc/rpm/sysinfo/Dirnames

		if [ ! -z "$providename" ]; then
			cat /dev/null > ${target_rootfs}/etc/rpm/sysinfo/Providename
			for provide in $providename ; do
				echo $provide >> ${target_rootfs}/etc/rpm/sysinfo/Providename
			done
		fi

		# Configure RPM... we enforce these settings!
		echo "Note: configuring RPM DB settings"
		mkdir -p ${target_rootfs}${rpmlibdir}
		mkdir -p ${target_rootfs}${rpmlibdir}/log
		# After change the __db.* cache size, log file will not be generated automatically,
		# that will raise some warnings, so touch a bare log for rpm write into it.
		touch ${target_rootfs}${rpmlibdir}/log/log.0000000001
		if [ ! -e ${target_rootfs}${rpmlibdir}/DB_CONFIG ]; then
			cat > ${target_rootfs}${rpmlibdir}/DB_CONFIG << EOF
# ================ Environment
set_data_dir .
set_create_dir .
set_lg_dir ./log
set_tmp_dir ./tmp
set_flags db_log_autoremove on

# -- thread_count must be >= 8
set_thread_count 64

# ================ Logging

# ================ Memory Pool
set_cachesize 0 1048576 0
set_mp_mmapsize 268435456

# ================ Locking
set_lk_max_locks 16384
set_lk_max_lockers 16384
set_lk_max_objects 16384
 mutex_set_max 163840

# ================ Replication
EOF
		fi

		# Create database so that smart doesn't complain (lazy init)
		rpm --root $target_rootfs --dbpath /var/lib/rpm -qa > /dev/null

		# Configure smart
		echo "Note: configuring Smart settings"
		rm -rf ${target_rootfs}/var/lib/smart
		smart --data-dir=${target_rootfs}/var/lib/smart config --set rpm-root=${target_rootfs}
		smart --data-dir=${target_rootfs}/var/lib/smart config --set rpm-dbpath=${rpmlibdir}
		smart --data-dir=${target_rootfs}/var/lib/smart config --set rpm-extra-macros._var=${localstatedir}
		smart --data-dir=${target_rootfs}/var/lib/smart config --set rpm-extra-macros._tmppath=/install/tmp
		package_write_smart_config ${target_rootfs}
		# Do the following configurations here, to avoid them being saved for field upgrade
		if [ "x${NO_RECOMMENDATIONS}" = "x1" ]; then
			smart --data-dir=${target_rootfs}/var/lib/smart config --set ignore-all-recommends=1
		fi
		for i in ${PACKAGE_EXCLUDE}; do
			smart --data-dir=${target_rootfs}/var/lib/smart flag --set exclude-packages $i
		done

		# Optional debugging
		#smart --data-dir=${target_rootfs}/var/lib/smart config --set rpm-log-level=debug
		#smart --data-dir=${target_rootfs}/var/lib/smart config --set rpm-log-file=/tmp/smart-debug-logfile

		# Delay this until later...
		#smart --data-dir=${target_rootfs}/var/lib/smart channel --add rpmsys type=rpm-sys -y

		for canonical_arch in $INSTALL_PLATFORM_EXTRA_RPM; do
			arch=$(echo $canonical_arch | sed "s,\([^-]*\)-.*,\1,")
			if [ -d ${DEPLOY_DIR_RPM}/$arch -a ! -e ${target_rootfs}/install/channel.$arch.stamp ] ; then
				echo "Note: adding Smart channel $arch ($channel_priority)"
				smart --data-dir=${target_rootfs}/var/lib/smart channel --add $arch type=rpm-md type=rpm-md baseurl=${DEPLOY_DIR_RPM}/$arch -y
				smart --data-dir=${target_rootfs}/var/lib/smart channel --set $arch priority=$channel_priority
				touch ${target_rootfs}/install/channel.$arch.stamp
			fi
			channel_priority=$(expr $channel_priority - 5)
		done
	fi

	# Construct install scriptlet wrapper
	cat << EOF > ${WORKDIR}/scriptlet_wrapper
#!/bin/bash

export PATH="${PATH}"
export D="${target_rootfs}"
export OFFLINE_ROOT="\$D"
export IPKG_OFFLINE_ROOT="\$D"
export OPKG_OFFLINE_ROOT="\$D"
export INTERCEPT_DIR="${WORKDIR}/intercept_scripts"
export NATIVE_ROOT=${STAGING_DIR_NATIVE}

\$2 \$1/\$3 \$4
if [ \$? -ne 0 ]; then
  if [ \$4 -eq 1 ]; then
    mkdir -p \$1/etc/rpm-postinsts
    name=\`head -1 \$1/\$3 | cut -d' ' -f 2\`
    echo "#!\$2" > \$1/etc/rpm-postinsts/\${name}
    echo "# Arg: \$4" >> \$1/etc/rpm-postinsts/\${name}
    cat \$1/\$3 >> \$1/etc/rpm-postinsts/\${name}
    chmod +x \$1/etc/rpm-postinsts/\${name}
  else
    echo "Error: pre/post remove scriptlet failed"
  fi
fi
EOF

	echo "Note: configuring RPM cross-install scriptlet_wrapper"
	chmod 0755 ${WORKDIR}/scriptlet_wrapper
	smart --data-dir=${target_rootfs}/var/lib/smart config --set rpm-extra-macros._cross_scriptlet_wrapper=${WORKDIR}/scriptlet_wrapper

	# Determine what to install
	translate_oe_to_smart ${sdk_mode} ${package_to_install} ${package_linguas}

	# If incremental install, we need to determine what we've got,
	# what we need to add, and what to remove...
	if [ "${INC_RPM_IMAGE_GEN}" = "1" -a "${INSTALL_COMPLEMENTARY_RPM}" != "1" ]; then
		# Dump the new solution
		echo "Note: creating install solution for incremental install"
		smart --data-dir=${target_rootfs}/var/lib/smart install -y --dump ${pkgs_to_install} 2> ${target_rootfs}/../solution.manifest
	fi

	if [ "${INSTALL_COMPLEMENTARY_RPM}" != "1" ]; then
		echo "Note: adding Smart RPM DB channel"
		smart --data-dir=${target_rootfs}/var/lib/smart channel --add rpmsys type=rpm-sys -y
	fi

	# If incremental install, we need to determine what we've got,
	# what we need to add, and what to remove...
	if [ "${INC_RPM_IMAGE_GEN}" = "1" -a "${INSTALL_COMPLEMENTARY_RPM}" != "1" ]; then
		# First upgrade everything that was previously installed to the latest version
		echo "Note: incremental update -- upgrade packages in place"
		smart --data-dir=${target_rootfs}/var/lib/smart upgrade

		# Dump what is already installed
		echo "Note: dump installed packages for incremental update"
		smart --data-dir=${target_rootfs}/var/lib/smart query --installed --output ${target_rootfs}/../installed.manifest

		sort ${target_rootfs}/../installed.manifest > ${target_rootfs}/../installed.manifest.sorted
		sort ${target_rootfs}/../solution.manifest > ${target_rootfs}/../solution.manifest.sorted
		
		comm -1 -3 ${target_rootfs}/../solution.manifest.sorted ${target_rootfs}/../installed.manifest.sorted \
			> ${target_rootfs}/../remove.list
		comm -2 -3 ${target_rootfs}/../solution.manifest.sorted ${target_rootfs}/../installed.manifest.sorted \
			> ${target_rootfs}/../install.list
		
		pkgs_to_remove=`cat ${target_rootfs}/../remove.list | xargs echo`
		pkgs_to_install=`cat ${target_rootfs}/../install.list | xargs echo`
		
		echo "Note: to be removed: ${pkgs_to_remove}"

		for pkg in ${pkgs_to_remove}; do
			echo "Debug: What required: $pkg"
			smart --data-dir=${target_rootfs}/var/lib/smart query $pkg --show-requiredby
		done

		[ -n "$pkgs_to_remove" ] && smart --data-dir=${target_rootfs}/var/lib/smart remove -y ${pkgs_to_remove}
	fi

	echo "Note: to be installed: ${pkgs_to_install}"
	[ -n "$pkgs_to_install" ] && smart --data-dir=${target_rootfs}/var/lib/smart install -y ${pkgs_to_install}

	if [ -n "${package_attemptonly}" ]; then
		echo "Note: installing attempt only packages..."
		echo "Attempting $pkgs_to_install"
		echo "Note: see `dirname ${BB_LOGFILE}`/log.do_${task}_attemptonly.${PID}"
		translate_oe_to_smart ${sdk_mode} --attemptonly $package_attemptonly
		for each_pkg in $pkgs_to_install ;  do
			# We need to try each package individually as a single dependency failure
			# will break the whole set otherwise.
			smart --data-dir=${target_rootfs}/var/lib/smart install -y $each_pkg >> "`dirname ${BB_LOGFILE}`/log.do_${task}_attemptonly.${PID}" 2>&1 || true
		done
	fi
}

# Construct per file dependencies file
def write_rpm_perfiledata(srcname, d):
    workdir = d.getVar('WORKDIR', True)
    packages = d.getVar('PACKAGES', True)
    pkgd = d.getVar('PKGD', True)

    def dump_filerdeps(varname, outfile, d):
        outfile.write("#!/usr/bin/env python\n\n")
        outfile.write("# Dependency table\n")
        outfile.write('deps = {\n')
        for pkg in packages.split():
            dependsflist_key = 'FILE' + varname + 'FLIST' + "_" + pkg
            dependsflist = (d.getVar(dependsflist_key, True) or "")
            for dfile in dependsflist.split():
                key = "FILE" + varname + "_" + dfile + "_" + pkg
                depends_dict = bb.utils.explode_dep_versions(d.getVar(key, True) or "")
                file = dfile.replace("@underscore@", "_")
                file = file.replace("@closebrace@", "]")
                file = file.replace("@openbrace@", "[")
                file = file.replace("@tab@", "\t")
                file = file.replace("@space@", " ")
                file = file.replace("@at@", "@")
                outfile.write('"' + pkgd + file + '" : "')
                for dep in depends_dict:
                    ver = depends_dict[dep]
                    if dep and ver:
                        ver = ver.replace("(","")
                        ver = ver.replace(")","")
                        outfile.write(dep + " " + ver + " ")
                    else:
                        outfile.write(dep + " ")
                outfile.write('",\n')
        outfile.write('}\n\n')
        outfile.write("import sys\n")
        outfile.write("while 1:\n")
        outfile.write("\tline = sys.stdin.readline().strip()\n")
        outfile.write("\tif not line:\n")
        outfile.write("\t\tsys.exit(0)\n")
        outfile.write("\tif line in deps:\n")
        outfile.write("\t\tprint(deps[line] + '\\n')\n")

    # OE-core dependencies a.k.a. RPM requires
    outdepends = workdir + "/" + srcname + ".requires"

    try:
        dependsfile = open(outdepends, 'w')
    except OSError:
        raise bb.build.FuncFailed("unable to open spec file for writing.")

    dump_filerdeps('RDEPENDS', dependsfile, d)

    dependsfile.close()
    os.chmod(outdepends, 0755)

    # OE-core / RPM Provides
    outprovides = workdir + "/" + srcname + ".provides"

    try:
        providesfile = open(outprovides, 'w')
    except OSError:
        raise bb.build.FuncFailed("unable to open spec file for writing.")

    dump_filerdeps('RPROVIDES', providesfile, d)

    providesfile.close()
    os.chmod(outprovides, 0755)

    return (outdepends, outprovides)


python write_specfile () {
    import oe.packagedata

    # append information for logs and patches to %prep
    def add_prep(d,spec_files_bottom):
        if d.getVar('SOURCE_ARCHIVE_PACKAGE_TYPE', True) == 'srpm':
            spec_files_bottom.append('%%prep -n %s' % d.getVar('PN', True) )
            spec_files_bottom.append('%s' % "echo \"include logs and patches, Please check them in SOURCES\"")
            spec_files_bottom.append('')

    # append the name of tarball to key word 'SOURCE' in xxx.spec.
    def tail_source(d):
        if d.getVar('SOURCE_ARCHIVE_PACKAGE_TYPE', True) == 'srpm':
            source_list = get_package(d)
            source_number = 0
            workdir = d.getVar('WORKDIR', True)
            for source in source_list:
                # The rpmbuild doesn't need the root permission, but it needs
                # to know the file's user and group name, the only user and
                # group in fakeroot is "root" when working in fakeroot.
                os.chown("%s/%s" % (workdir, source), 0, 0)
                spec_preamble_top.append('Source' + str(source_number) + ': %s' % source)
                source_number += 1
    # We need a simple way to remove the MLPREFIX from the package name,
    # and dependency information...
    def strip_multilib(name, d):
        multilibs = d.getVar('MULTILIBS', True) or ""
        for ext in multilibs.split():
            eext = ext.split(':')
            if len(eext) > 1 and eext[0] == 'multilib' and name and name.find(eext[1] + '-') >= 0:
                name = "".join(name.split(eext[1] + '-'))
        return name

    def strip_multilib_deps(deps, d):
        depends = bb.utils.explode_dep_versions2(deps or "")
        newdeps = {}
        for dep in depends:
            newdeps[strip_multilib(dep, d)] = depends[dep]
        return bb.utils.join_deps(newdeps)

#        ml = d.getVar("MLPREFIX", True)
#        if ml and name and len(ml) != 0 and name.find(ml) == 0:
#            return ml.join(name.split(ml, 1)[1:])
#        return name

    # In RPM, dependencies are of the format: pkg <>= Epoch:Version-Release
    # This format is similar to OE, however there are restrictions on the
    # characters that can be in a field.  In the Version field, "-"
    # characters are not allowed.  "-" is allowed in the Release field.
    #
    # We translate the "-" in the version to a "+", by loading the PKGV
    # from the dependent recipe, replacing the - with a +, and then using
    # that value to do a replace inside of this recipe's dependencies.
    # This preserves the "-" separator between the version and release, as
    # well as any "-" characters inside of the release field.
    #
    # All of this has to happen BEFORE the mapping_rename_hook as
    # after renaming we cannot look up the dependencies in the packagedata
    # store.
    def translate_vers(varname, d):
        depends = d.getVar(varname, True)
        if depends:
            depends_dict = bb.utils.explode_dep_versions2(depends)
            newdeps_dict = {}
            for dep in depends_dict:
                verlist = []
                for ver in depends_dict[dep]:
                    if '-' in ver:
                        subd = oe.packagedata.read_subpkgdata_dict(dep, d)
                        if 'PKGV' in subd:
                            pv = subd['PV']
                            pkgv = subd['PKGV']
                            reppv = pkgv.replace('-', '+')
                            ver = ver.replace(pv, reppv).replace(pkgv, reppv)
                        if 'PKGR' in subd:
                            # Make sure PKGR rather than PR in ver
                            pr = '-' + subd['PR']
                            pkgr = '-' + subd['PKGR']
                            if pkgr not in ver:
                                ver = ver.replace(pr, pkgr)
                        verlist.append(ver)
                    else:
                        verlist.append(ver)
                newdeps_dict[dep] = verlist
            depends = bb.utils.join_deps(newdeps_dict)
            d.setVar(varname, depends.strip())

    # We need to change the style the dependency from BB to RPM
    # This needs to happen AFTER the mapping_rename_hook
    def print_deps(variable, tag, array, d):
        depends = variable
        if depends:
            depends_dict = bb.utils.explode_dep_versions2(depends)
            for dep in depends_dict:
                for ver in depends_dict[dep]:
                    ver = ver.replace('(', '')
                    ver = ver.replace(')', '')
                    array.append("%s: %s %s" % (tag, dep, ver))
                if not len(depends_dict[dep]):
                    array.append("%s: %s" % (tag, dep))

    def walk_files(walkpath, target, conffiles):
        for rootpath, dirs, files in os.walk(walkpath):
            path = rootpath.replace(walkpath, "")
            for dir in dirs:
                # All packages own the directories their files are in...
                target.append('%dir "' + path + '/' + dir + '"')
            for file in files:
                if conffiles.count(path + '/' + file):
                    target.append('%config "' + path + '/' + file + '"')
                else:
                    target.append('"' + path + '/' + file + '"')

    # Prevent the prerm/postrm scripts from being run during an upgrade
    def wrap_uninstall(scriptvar):
        scr = scriptvar.strip()
        if scr.startswith("#!"):
            pos = scr.find("\n") + 1
        else:
            pos = 0
        scr = scr[:pos] + 'if [ "$1" = "0" ] ; then\n' + scr[pos:] + '\nfi'
        return scr

    def get_perfile(varname, pkg, d):
        deps = []
        dependsflist_key = 'FILE' + varname + 'FLIST' + "_" + pkg
        dependsflist = (d.getVar(dependsflist_key, True) or "")
        for dfile in dependsflist.split():
            key = "FILE" + varname + "_" + dfile + "_" + pkg
            depends = d.getVar(key, True)
            if depends:
                deps.append(depends)
        return " ".join(deps)

    def append_description(spec_preamble, text):
        """
        Add the description to the spec file.
        """
        import textwrap
        dedent_text = textwrap.dedent(text).strip()
        # Bitbake saves "\n" as "\\n"
        if '\\n' in dedent_text:
            for t in dedent_text.split('\\n'):
                spec_preamble.append(t.strip())
        else:
            spec_preamble.append('%s' % textwrap.fill(dedent_text, width=75))

    packages = d.getVar('PACKAGES', True)
    if not packages or packages == '':
        bb.debug(1, "No packages; nothing to do")
        return

    pkgdest = d.getVar('PKGDEST', True)
    if not pkgdest:
        bb.fatal("No PKGDEST")

    outspecfile = d.getVar('OUTSPECFILE', True)
    if not outspecfile:
        bb.fatal("No OUTSPECFILE")

    # Construct the SPEC file...
    srcname    = strip_multilib(d.getVar('PN', True), d)
    srcsummary = (d.getVar('SUMMARY', True) or d.getVar('DESCRIPTION', True) or ".")
    srcversion = d.getVar('PKGV', True).replace('-', '+')
    srcrelease = d.getVar('PKGR', True)
    srcepoch   = (d.getVar('PKGE', True) or "")
    srclicense = d.getVar('LICENSE', True)
    srcsection = d.getVar('SECTION', True)
    srcmaintainer  = d.getVar('MAINTAINER', True)
    srchomepage    = d.getVar('HOMEPAGE', True)
    srcdescription = d.getVar('DESCRIPTION', True) or "."

    srcdepends     = strip_multilib_deps(d.getVar('DEPENDS', True), d)
    srcrdepends    = []
    srcrrecommends = []
    srcrsuggests   = []
    srcrprovides   = []
    srcrreplaces   = []
    srcrconflicts  = []
    srcrobsoletes  = []

    srcrpreinst  = []
    srcrpostinst = []
    srcrprerm    = []
    srcrpostrm   = []

    spec_preamble_top = []
    spec_preamble_bottom = []

    spec_scriptlets_top = []
    spec_scriptlets_bottom = []

    spec_files_top = []
    spec_files_bottom = []

    perfiledeps = (d.getVar("MERGEPERFILEDEPS", True) or "0") == "0"

    for pkg in packages.split():
        localdata = bb.data.createCopy(d)

        root = "%s/%s" % (pkgdest, pkg)

        lf = bb.utils.lockfile(root + ".lock")

        localdata.setVar('ROOT', '')
        localdata.setVar('ROOT_%s' % pkg, root)
        pkgname = localdata.getVar('PKG_%s' % pkg, True)
        if not pkgname:
            pkgname = pkg
        localdata.setVar('PKG', pkgname)

        localdata.setVar('OVERRIDES', pkg)

        bb.data.update_data(localdata)

        conffiles = (localdata.getVar('CONFFILES', True) or "").split()

        splitname    = strip_multilib(pkgname, d)

        splitsummary = (localdata.getVar('SUMMARY', True) or localdata.getVar('DESCRIPTION', True) or ".")
        splitversion = (localdata.getVar('PKGV', True) or "").replace('-', '+')
        splitrelease = (localdata.getVar('PKGR', True) or "")
        splitepoch   = (localdata.getVar('PKGE', True) or "")
        splitlicense = (localdata.getVar('LICENSE', True) or "")
        splitsection = (localdata.getVar('SECTION', True) or "")
        splitdescription = (localdata.getVar('DESCRIPTION', True) or ".")

        translate_vers('RDEPENDS', localdata)
        translate_vers('RRECOMMENDS', localdata)
        translate_vers('RSUGGESTS', localdata)
        translate_vers('RPROVIDES', localdata)
        translate_vers('RREPLACES', localdata)
        translate_vers('RCONFLICTS', localdata)

        # Map the dependencies into their final form
        mapping_rename_hook(localdata)

        splitrdepends    = strip_multilib_deps(localdata.getVar('RDEPENDS', True), d)
        splitrrecommends = strip_multilib_deps(localdata.getVar('RRECOMMENDS', True), d)
        splitrsuggests   = strip_multilib_deps(localdata.getVar('RSUGGESTS', True), d)
        splitrprovides   = strip_multilib_deps(localdata.getVar('RPROVIDES', True), d)
        splitrreplaces   = strip_multilib_deps(localdata.getVar('RREPLACES', True), d)
        splitrconflicts  = strip_multilib_deps(localdata.getVar('RCONFLICTS', True), d)
        splitrobsoletes  = []

        splitrpreinst  = localdata.getVar('pkg_preinst', True)
        splitrpostinst = localdata.getVar('pkg_postinst', True)
        splitrprerm    = localdata.getVar('pkg_prerm', True)
        splitrpostrm   = localdata.getVar('pkg_postrm', True)


        if not perfiledeps:
            # Add in summary of per file dependencies
            splitrdepends = splitrdepends + " " + get_perfile('RDEPENDS', pkg, d)
            splitrprovides = splitrprovides + " " + get_perfile('RPROVIDES', pkg, d)

        # Gather special src/first package data
        if srcname == splitname:
            srcrdepends    = splitrdepends
            srcrrecommends = splitrrecommends
            srcrsuggests   = splitrsuggests
            srcrprovides   = splitrprovides
            srcrreplaces   = splitrreplaces
            srcrconflicts  = splitrconflicts

            srcrpreinst    = splitrpreinst
            srcrpostinst   = splitrpostinst
            srcrprerm      = splitrprerm
            srcrpostrm     = splitrpostrm

            file_list = []
            walk_files(root, file_list, conffiles)
            if not file_list and localdata.getVar('ALLOW_EMPTY') != "1":
                bb.note("Not creating empty RPM package for %s" % splitname)
            else:
                bb.note("Creating RPM package for %s" % splitname)
                spec_files_top.append('%files')
                spec_files_top.append('%defattr(-,-,-,-)')
                if file_list:
                    bb.note("Creating RPM package for %s" % splitname)
                    spec_files_top.extend(file_list)
                else:
                    bb.note("Creating EMPTY RPM Package for %s" % splitname)
                spec_files_top.append('')

            bb.utils.unlockfile(lf)
            continue

        # Process subpackage data
        spec_preamble_bottom.append('%%package -n %s' % splitname)
        spec_preamble_bottom.append('Summary: %s' % splitsummary)
        if srcversion != splitversion:
            spec_preamble_bottom.append('Version: %s' % splitversion)
        if srcrelease != splitrelease:
            spec_preamble_bottom.append('Release: %s' % splitrelease)
        if srcepoch != splitepoch:
            spec_preamble_bottom.append('Epoch: %s' % splitepoch)
        if srclicense != splitlicense:
            spec_preamble_bottom.append('License: %s' % splitlicense)
        spec_preamble_bottom.append('Group: %s' % splitsection)

        # Replaces == Obsoletes && Provides
        robsoletes = bb.utils.explode_dep_versions2(splitrobsoletes or "")
        rprovides = bb.utils.explode_dep_versions2(splitrprovides or "")
        rreplaces = bb.utils.explode_dep_versions2(splitrreplaces or "")
        for dep in rreplaces:
            if not dep in robsoletes:
                robsoletes[dep] = rreplaces[dep]
            if not dep in rprovides:
                rprovides[dep] = rreplaces[dep]
        splitrobsoletes = bb.utils.join_deps(robsoletes, commasep=False)
        splitrprovides = bb.utils.join_deps(rprovides, commasep=False)

        print_deps(splitrdepends, "Requires", spec_preamble_bottom, d)
        if splitrpreinst:
            print_deps(splitrdepends, "Requires(pre)", spec_preamble_bottom, d)
        if splitrpostinst:
            print_deps(splitrdepends, "Requires(post)", spec_preamble_bottom, d)
        if splitrprerm:
            print_deps(splitrdepends, "Requires(preun)", spec_preamble_bottom, d)
        if splitrpostrm:
            print_deps(splitrdepends, "Requires(postun)", spec_preamble_bottom, d)

        # Suggests in RPM are like recommends in OE-core!
        print_deps(splitrrecommends, "Suggests", spec_preamble_bottom, d)
        # While there is no analog for suggests... (So call them recommends for now)
        print_deps(splitrsuggests,  "Recommends", spec_preamble_bottom, d)
        print_deps(splitrprovides,  "Provides", spec_preamble_bottom, d)
        print_deps(splitrobsoletes, "Obsoletes", spec_preamble_bottom, d)

        # conflicts can not be in a provide!  We will need to filter it.
        if splitrconflicts:
            depends_dict = bb.utils.explode_dep_versions2(splitrconflicts)
            newdeps_dict = {}
            for dep in depends_dict:
                if dep not in splitrprovides:
                    newdeps_dict[dep] = depends_dict[dep]
            if newdeps_dict:
                splitrconflicts = bb.utils.join_deps(newdeps_dict)
            else:
                splitrconflicts = ""

        print_deps(splitrconflicts,  "Conflicts", spec_preamble_bottom, d)

        spec_preamble_bottom.append('')

        spec_preamble_bottom.append('%%description -n %s' % splitname)
        append_description(spec_preamble_bottom, splitdescription)

        spec_preamble_bottom.append('')

        # Now process scriptlets
        if splitrpreinst:
            spec_scriptlets_bottom.append('%%pre -n %s' % splitname)
            spec_scriptlets_bottom.append('# %s - preinst' % splitname)
            spec_scriptlets_bottom.append(splitrpreinst)
            spec_scriptlets_bottom.append('')
        if splitrpostinst:
            spec_scriptlets_bottom.append('%%post -n %s' % splitname)
            spec_scriptlets_bottom.append('# %s - postinst' % splitname)
            spec_scriptlets_bottom.append(splitrpostinst)
            spec_scriptlets_bottom.append('')
        if splitrprerm:
            spec_scriptlets_bottom.append('%%preun -n %s' % splitname)
            spec_scriptlets_bottom.append('# %s - prerm' % splitname)
            scriptvar = wrap_uninstall(splitrprerm)
            spec_scriptlets_bottom.append(scriptvar)
            spec_scriptlets_bottom.append('')
        if splitrpostrm:
            spec_scriptlets_bottom.append('%%postun -n %s' % splitname)
            spec_scriptlets_bottom.append('# %s - postrm' % splitname)
            scriptvar = wrap_uninstall(splitrpostrm)
            spec_scriptlets_bottom.append(scriptvar)
            spec_scriptlets_bottom.append('')

        # Now process files
        file_list = []
        walk_files(root, file_list, conffiles)
        if not file_list and localdata.getVar('ALLOW_EMPTY') != "1":
            bb.note("Not creating empty RPM package for %s" % splitname)
        else:
            spec_files_bottom.append('%%files -n %s' % splitname)
            spec_files_bottom.append('%defattr(-,-,-,-)')
            if file_list:
                bb.note("Creating RPM package for %s" % splitname)
                spec_files_bottom.extend(file_list)
            else:
                bb.note("Creating EMPTY RPM Package for %s" % splitname)
            spec_files_bottom.append('')

        del localdata
        bb.utils.unlockfile(lf)
    
    add_prep(d,spec_files_bottom)
    spec_preamble_top.append('Summary: %s' % srcsummary)
    spec_preamble_top.append('Name: %s' % srcname)
    spec_preamble_top.append('Version: %s' % srcversion)
    spec_preamble_top.append('Release: %s' % srcrelease)
    if srcepoch and srcepoch.strip() != "":
        spec_preamble_top.append('Epoch: %s' % srcepoch)
    spec_preamble_top.append('License: %s' % srclicense)
    spec_preamble_top.append('Group: %s' % srcsection)
    spec_preamble_top.append('Packager: %s' % srcmaintainer)
    spec_preamble_top.append('URL: %s' % srchomepage)
    tail_source(d)

    # Replaces == Obsoletes && Provides
    robsoletes = bb.utils.explode_dep_versions2(srcrobsoletes or "")
    rprovides = bb.utils.explode_dep_versions2(srcrprovides or "")
    rreplaces = bb.utils.explode_dep_versions2(srcrreplaces or "")
    for dep in rreplaces:
        if not dep in robsoletes:
            robsoletes[dep] = rreplaces[dep]
        if not dep in rprovides:
            rprovides[dep] = rreplaces[dep]
    srcrobsoletes = bb.utils.join_deps(robsoletes, commasep=False)
    srcrprovides = bb.utils.join_deps(rprovides, commasep=False)

    print_deps(srcdepends, "BuildRequires", spec_preamble_top, d)
    print_deps(srcrdepends, "Requires", spec_preamble_top, d)
    if srcrpreinst:
        print_deps(srcrdepends, "Requires(pre)", spec_preamble_top, d)
    if srcrpostinst:
        print_deps(srcrdepends, "Requires(post)", spec_preamble_top, d)
    if srcrprerm:
        print_deps(srcrdepends, "Requires(preun)", spec_preamble_top, d)
    if srcrpostrm:
        print_deps(srcrdepends, "Requires(postun)", spec_preamble_top, d)

    # Suggests in RPM are like recommends in OE-core!
    print_deps(srcrrecommends, "Suggests", spec_preamble_top, d)
    # While there is no analog for suggests... (So call them recommends for now)
    print_deps(srcrsuggests, "Recommends", spec_preamble_top, d)
    print_deps(srcrprovides, "Provides", spec_preamble_top, d)
    print_deps(srcrobsoletes, "Obsoletes", spec_preamble_top, d)
    
    # conflicts can not be in a provide!  We will need to filter it.
    if srcrconflicts:
        depends_dict = bb.utils.explode_dep_versions2(srcrconflicts)
        newdeps_dict = {}
        for dep in depends_dict:
            if dep not in srcrprovides:
                newdeps_dict[dep] = depends_dict[dep]
        if newdeps_dict:
            srcrconflicts = bb.utils.join_deps(newdeps_dict)
        else:
            srcrconflicts = ""

    print_deps(srcrconflicts, "Conflicts", spec_preamble_top, d)

    spec_preamble_top.append('')

    spec_preamble_top.append('%description')
    append_description(spec_preamble_top, srcdescription)

    spec_preamble_top.append('')

    if srcrpreinst:
        spec_scriptlets_top.append('%pre')
        spec_scriptlets_top.append('# %s - preinst' % srcname)
        spec_scriptlets_top.append(srcrpreinst)
        spec_scriptlets_top.append('')
    if srcrpostinst:
        spec_scriptlets_top.append('%post')
        spec_scriptlets_top.append('# %s - postinst' % srcname)
        spec_scriptlets_top.append(srcrpostinst)
        spec_scriptlets_top.append('')
    if srcrprerm:
        spec_scriptlets_top.append('%preun')
        spec_scriptlets_top.append('# %s - prerm' % srcname)
        scriptvar = wrap_uninstall(srcrprerm)
        spec_scriptlets_top.append(scriptvar)
        spec_scriptlets_top.append('')
    if srcrpostrm:
        spec_scriptlets_top.append('%postun')
        spec_scriptlets_top.append('# %s - postrm' % srcname)
        scriptvar = wrap_uninstall(srcrpostrm)
        spec_scriptlets_top.append(scriptvar)
        spec_scriptlets_top.append('')

    # Write the SPEC file
    try:
        specfile = open(outspecfile, 'w')
    except OSError:
        raise bb.build.FuncFailed("unable to open spec file for writing.")

    # RPMSPEC_PREAMBLE is a way to add arbitrary text to the top
    # of the generated spec file
    external_preamble = d.getVar("RPMSPEC_PREAMBLE", True)
    if external_preamble:
        specfile.write(external_preamble + "\n")

    for line in spec_preamble_top:
        specfile.write(line + "\n")

    for line in spec_preamble_bottom:
        specfile.write(line + "\n")

    for line in spec_scriptlets_top:
        specfile.write(line + "\n")

    for line in spec_scriptlets_bottom:
        specfile.write(line + "\n")

    for line in spec_files_top:
        specfile.write(line + "\n")

    for line in spec_files_bottom:
        specfile.write(line + "\n")

    specfile.close()
}

python do_package_rpm () {
    def creat_srpm_dir(d):
        if d.getVar('SOURCE_ARCHIVE_PACKAGE_TYPE', True) == 'srpm':
            clean_licenses = get_licenses(d)
            pkgwritesrpmdir = bb.data.expand('${PKGWRITEDIRSRPM}/${PACKAGE_ARCH_EXTEND}', d)
            pkgwritesrpmdir = pkgwritesrpmdir + '/' + clean_licenses
            bb.utils.mkdirhier(pkgwritesrpmdir)
            os.chmod(pkgwritesrpmdir, 0755)
            return pkgwritesrpmdir
            
    # We need a simple way to remove the MLPREFIX from the package name,
    # and dependency information...
    def strip_multilib(name, d):
        ml = d.getVar("MLPREFIX", True)
        if ml and name and len(ml) != 0 and name.find(ml) >= 0:
            return "".join(name.split(ml))
        return name

    workdir = d.getVar('WORKDIR', True)
    outdir = d.getVar('DEPLOY_DIR_IPK', True)
    tmpdir = d.getVar('TMPDIR', True)
    pkgd = d.getVar('PKGD', True)
    pkgdest = d.getVar('PKGDEST', True)
    if not workdir or not outdir or not pkgd or not tmpdir:
        bb.error("Variables incorrectly set, unable to package")
        return

    packages = d.getVar('PACKAGES', True)
    if not packages or packages == '':
        bb.debug(1, "No packages; nothing to do")
        return

    # Construct the spec file...
    # If the spec file already exist, and has not been stored into 
    # pseudo's files.db, it maybe cause rpmbuild src.rpm fail,
    # so remove it before doing rpmbuild src.rpm.
    srcname    = strip_multilib(d.getVar('PN', True), d)
    outspecfile = workdir + "/" + srcname + ".spec"
    if os.path.isfile(outspecfile):
        os.remove(outspecfile)
    d.setVar('OUTSPECFILE', outspecfile)
    bb.build.exec_func('write_specfile', d)

    perfiledeps = (d.getVar("MERGEPERFILEDEPS", True) or "0") == "0"
    if perfiledeps:
        outdepends, outprovides = write_rpm_perfiledata(srcname, d)

    # Setup the rpmbuild arguments...
    rpmbuild = d.getVar('RPMBUILD', True)
    targetsys = d.getVar('TARGET_SYS', True)
    targetvendor = d.getVar('TARGET_VENDOR', True)
    package_arch = (d.getVar('PACKAGE_ARCH', True) or "").replace("-", "_")
    if package_arch not in "all any noarch".split() and not package_arch.endswith("_nativesdk"):
        ml_prefix = (d.getVar('MLPREFIX', True) or "").replace("-", "_")
        d.setVar('PACKAGE_ARCH_EXTEND', ml_prefix + package_arch)
    else:
        d.setVar('PACKAGE_ARCH_EXTEND', package_arch)
    pkgwritedir = d.expand('${PKGWRITEDIRRPM}/${PACKAGE_ARCH_EXTEND}')
    pkgarch = d.expand('${PACKAGE_ARCH_EXTEND}${TARGET_VENDOR}-${TARGET_OS}')
    magicfile = d.expand('${STAGING_DIR_NATIVE}${datadir_native}/misc/magic.mgc')
    bb.utils.mkdirhier(pkgwritedir)
    os.chmod(pkgwritedir, 0755)

    cmd = rpmbuild
    cmd = cmd + " --nodeps --short-circuit --target " + pkgarch + " --buildroot " + pkgd
    cmd = cmd + " --define '_topdir " + workdir + "' --define '_rpmdir " + pkgwritedir + "'"
    cmd = cmd + " --define '_build_name_fmt %%{NAME}-%%{VERSION}-%%{RELEASE}.%%{ARCH}.rpm'"
    cmd = cmd + " --define '_use_internal_dependency_generator 0'"
    if perfiledeps:
        cmd = cmd + " --define '__find_requires " + outdepends + "'"
        cmd = cmd + " --define '__find_provides " + outprovides + "'"
    else:
        cmd = cmd + " --define '__find_requires %{nil}'"
        cmd = cmd + " --define '__find_provides %{nil}'"
    cmd = cmd + " --define '_unpackaged_files_terminate_build 0'"
    cmd = cmd + " --define 'debug_package %{nil}'"
    cmd = cmd + " --define '_rpmfc_magic_path " + magicfile + "'"
    cmd = cmd + " --define '_tmppath " + workdir + "'"
    if d.getVar('SOURCE_ARCHIVE_PACKAGE_TYPE', True) == 'srpm':
        cmd = cmd + " --define '_sourcedir " + workdir + "'"
        cmdsrpm = cmd + " --define '_srcrpmdir " + creat_srpm_dir(d) + "'"
        cmdsrpm = cmdsrpm + " -bs " + outspecfile
        # Build the .src.rpm
        d.setVar('SBUILDSPEC', cmdsrpm + "\n")
        d.setVarFlag('SBUILDSPEC', 'func', '1')
        bb.build.exec_func('SBUILDSPEC', d)
        # Remove the source (SOURCE0, SOURCE1 ...)
        cmd = cmd + " --rmsource "
    cmd = cmd + " -bb " + outspecfile

    # Build the rpm package!
    d.setVar('BUILDSPEC', cmd + "\n")
    d.setVarFlag('BUILDSPEC', 'func', '1')
    bb.build.exec_func('BUILDSPEC', d)
}

python () {
    if d.getVar('PACKAGES', True) != '':
        deps = ' rpm-native:do_populate_sysroot virtual/fakeroot-native:do_populate_sysroot'
        d.appendVarFlag('do_package_write_rpm', 'depends', deps)
        d.setVarFlag('do_package_write_rpm', 'fakeroot', 1)
}

SSTATETASKS += "do_package_write_rpm"
do_package_write_rpm[sstate-name] = "deploy-rpm"
do_package_write_rpm[sstate-inputdirs] = "${PKGWRITEDIRRPM}"
do_package_write_rpm[sstate-outputdirs] = "${DEPLOY_DIR_RPM}"
# Take a shared lock, we can write multiple packages at the same time...
# but we need to stop the rootfs/solver from running while we do...
do_package_write_rpm[sstate-lockfile-shared] += "${DEPLOY_DIR_RPM}/rpm.lock"

python do_package_write_rpm_setscene () {
    sstate_setscene(d)
}
addtask do_package_write_rpm_setscene

python do_package_write_rpm () {
    bb.build.exec_func("read_subpackage_metadata", d)
    bb.build.exec_func("do_package_rpm", d)
}

do_package_write_rpm[dirs] = "${PKGWRITEDIRRPM}"
do_package_write_rpm[cleandirs] = "${PKGWRITEDIRRPM}"
do_package_write_rpm[umask] = "022"
addtask package_write_rpm before do_package_write after do_packagedata do_package

PACKAGEINDEXES += "[ ! -e ${DEPLOY_DIR_RPM} ] || package_update_index_rpm;"
PACKAGEINDEXDEPS += "rpm-native:do_populate_sysroot"
PACKAGEINDEXDEPS += "createrepo-native:do_populate_sysroot"
