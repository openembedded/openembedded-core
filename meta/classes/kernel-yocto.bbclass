S = "${WORKDIR}/linux"

# remove tasks that modify the source tree in case externalsrc is inherited
SRCTREECOVEREDTASKS += "do_kernel_link_vmlinux do_kernel_configme do_validate_branches do_kernel_configcheck do_kernel_checkout do_patch"

# returns local (absolute) path names for all valid patches in the
# src_uri
def find_patches(d):
    patches = src_patches(d)
    patch_list=[]
    for p in patches:
        _, _, local, _, _, _ = bb.fetch.decodeurl(p)
        patch_list.append(local)

    return patch_list

# returns all the elements from the src uri that are .scc files
def find_sccs(d):
    sources=src_patches(d, True)
    sources_list=[]
    for s in sources:
        base, ext = os.path.splitext(os.path.basename(s))
        if ext and ext in [".scc", ".cfg"]:
            sources_list.append(s)
        elif base and base in 'defconfig':
            sources_list.append(s)

    return sources_list

# check the SRC_URI for "kmeta" type'd git repositories. Return the name of
# the repository as it will be found in WORKDIR
def find_kernel_feature_dirs(d):
    feature_dirs=[]
    fetch = bb.fetch2.Fetch([], d)
    for url in fetch.urls:
        urldata = fetch.ud[url]
        parm = urldata.parm
        if "type" in parm:
            type = parm["type"]
        if "destsuffix" in parm:
            destdir = parm["destsuffix"]
            if type == "kmeta":
                feature_dirs.append(destdir)
	    
    return feature_dirs

# find the master/machine source branch. In the same way that the fetcher proceses
# git repositories in the SRC_URI we take the first repo found, first branch.
def get_machine_branch(d, default):
    fetch = bb.fetch2.Fetch([], d)
    for url in fetch.urls:
        urldata = fetch.ud[url]
        parm = urldata.parm
        if "branch" in parm:
            branches = urldata.parm.get("branch").split(',')
            return branches[0]
	    
    return default

do_patch() {
	cd ${S}
	export KMETA=${KMETA}

	# if kernel tools are available in-tree, they are preferred
	# and are placed on the path before any external tools. Unless
	# the external tools flag is set, in that case we do nothing.
	if [ -f "${S}/scripts/util/configme" ]; then
		if [ -z "${EXTERNAL_KERNEL_TOOLS}" ]; then
			PATH=${S}/scripts/util:${PATH}
		fi
	fi

	machine_branch="${@ get_machine_branch(d, "${KBRANCH}" )}"

	# if we have a defined/set meta branch we should not be generating
	# any meta data. The passed branch has what we need.
	if [ -n "${KMETA}" ]; then
		createme_flags="--disable-meta-gen --meta ${KMETA}"
	fi

	createme ${createme_flags} ${ARCH} ${machine_branch}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not create ${machine_branch}"
		exit 1
	fi

	sccs="${@" ".join(find_sccs(d))}"
	patches="${@" ".join(find_patches(d))}"
	feat_dirs="${@" ".join(find_kernel_feature_dirs(d))}"

	set +e
	# add any explicitly referenced features onto the end of the feature
	# list that is passed to the kernel build scripts.
	if [ -n "${KERNEL_FEATURES}" ]; then
		for feat in ${KERNEL_FEATURES}; do
			addon_features="$addon_features --feature $feat"
		done
	fi

	# check for feature directories/repos/branches that were part of the
	# SRC_URI. If they were supplied, we convert them into include directives
	# for the update part of the process
	if [ -n "${feat_dirs}" ]; then
	    for f in ${feat_dirs}; do
		if [ -d "${WORKDIR}/$f/meta" ]; then
		    includes="$includes -I${WORKDIR}/$f/meta"
		elif [ -d "${WORKDIR}/$f" ]; then
		    includes="$includes -I${WORKDIR}/$f"
		fi
	    done
	fi

	if [ "${machine_branch}" != "${KBRANCH_DEFAULT}" ]; then
		updateme_flags="--branch ${machine_branch}"
	fi

	# updates or generates the target description
	updateme ${updateme_flags} -DKDESC=${KMACHINE}:${LINUX_KERNEL_TYPE} \
                         ${includes} ${addon_features} ${ARCH} ${KMACHINE} ${sccs} ${patches}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not update ${machine_branch}"
		exit 1
	fi

	# executes and modifies the source tree as required
	patchme ${KMACHINE}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not apply patches for ${KMACHINE}."
		echo "       Patch failures can be resolved in the devshell (bitbake -c devshell ${PN})"
		exit 1
	fi

	# Perform a final check. If something other than the default kernel
	# branch was requested, and that's not where we ended up, then we 
	# should thrown an error, since we aren't building what was expected
	final_branch="$(git symbolic-ref HEAD 2>/dev/null)"
	final_branch=${final_branch##refs/heads/}
	if [ "${machine_branch}" != "${KBRANCH_DEFAULT}" ] &&
	   [ "${final_branch}" != "${machine_branch}" ]; then
		echo "ERROR: branch ${machine_branch} was requested, but was not properly"
		echo "       configured to be built. The current branch is ${final_branch}"
		exit 1
	fi
}

do_kernel_checkout() {
	set +e

	# A linux yocto SRC_URI should use the bareclone option. That
	# ensures that all the branches are available in the WORKDIR version
	# of the repository.
	source_dir=`echo ${S} | sed 's%/$%%'`
	source_workdir="${WORKDIR}/git"
	if [ -d "${WORKDIR}/git/" ] && [ -d "${WORKDIR}/git/.git" ]; then
		# case2: the repository is a non-bare clone

		# if S is WORKDIR/git, then we shouldn't be moving or deleting the tree.
		if [ "${source_dir}" != "${source_workdir}" ]; then
			rm -rf ${S}
			mv ${WORKDIR}/git ${S}
		fi
		cd ${S}
	elif [ -d "${WORKDIR}/git/" ] && [ ! -d "${WORKDIR}/git/.git" ]; then
		# case2: the repository is a bare clone

		# if S is WORKDIR/git, then we shouldn't be moving or deleting the tree.
		if [ "${source_dir}" != "${source_workdir}" ]; then
			rm -rf ${S}
			mkdir -p ${S}/.git
			mv ${WORKDIR}/git/* ${S}/.git
			rm -rf ${WORKDIR}/git/
		fi
		cd ${S}	
		git config core.bare false
	else
		# case 3: we have no git repository at all. 
		# To support low bandwidth options for building the kernel, we'll just 
		# convert the tree to a git repo and let the rest of the process work unchanged
		
		# if ${S} hasn't been set to the proper subdirectory a default of "linux" is 
		# used, but we can't initialize that empty directory. So check it and throw a
		# clear error

	        cd ${S}
		if [ ! -f "Makefile" ]; then
			echo "[ERROR]: S is not set to the linux source directory. Check "
			echo "         the recipe and set S to the proper extracted subdirectory"
			exit 1
		fi
		git init
		git add .
		git commit -q -m "baseline commit: creating repo for ${PN}-${PV}"
	fi
	# end debare

       	# If KMETA is defined, the branch must exist, but a machine branch
	# can be missing since it may be created later by the tools.
	if [ -n "${KMETA}" ]; then
		git branch -a | grep -q ${KMETA}
		if [ $? -ne 0 ]; then
			echo "ERROR. The branch '${KMETA}' is required and was not"
			echo "found. Ensure that the SRC_URI points to a valid linux-yocto"
			echo "kernel repository"
			exit 1
		fi
	fi
	
	machine_branch="${@ get_machine_branch(d, "${KBRANCH}" )}"

	if [ "${KBRANCH}" != "${machine_branch}" ]; then
		echo "WARNING: The SRC_URI machine branch and KBRANCH are not the same."
		echo "	       KBRANCH will be adjusted to match, but this typically is a"
		echo "	       misconfiguration and should be checked."
	fi

	# convert any remote branches to local tracking ones
	for i in `git branch -a | grep remotes | grep -v HEAD`; do
		b=`echo $i | cut -d' ' -f2 | sed 's%remotes/origin/%%'`;
		git show-ref --quiet --verify -- "refs/heads/$b"
		if [ $? -ne 0 ]; then
			git branch $b $i > /dev/null
		fi
	done

	# Create a working tree copy of the kernel by checking out a branch
	git show-ref --quiet --verify -- "refs/heads/${machine_branch}"
	if [ $? -eq 0 ]; then
		# checkout and clobber any unimportant files
		git checkout -f ${machine_branch}
	else
		echo "Not checking out ${machine_branch}, it will be created later"
		git checkout -f master
	fi
}
do_kernel_checkout[dirs] = "${S}"

addtask kernel_checkout before do_patch after do_unpack

do_kernel_configme[dirs] = "${S} ${B}"
do_kernel_configme() {
	echo "[INFO] doing kernel configme"
	export KMETA=${KMETA}

	if [ -n ${KCONFIG_MODE} ]; then
		configmeflags=${KCONFIG_MODE}
	else
		# If a defconfig was passed, use =n as the baseline, which is achieved
		# via --allnoconfig
		if [ -f ${WORKDIR}/defconfig ]; then
			configmeflags="--allnoconfig"
		fi
	fi

	cd ${S}
	PATH=${PATH}:${S}/scripts/util
	configme ${configmeflags} --reconfig --output ${B} ${LINUX_KERNEL_TYPE} ${KMACHINE}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not configure ${KMACHINE}-${LINUX_KERNEL_TYPE}"
		exit 1
	fi
	
	echo "# Global settings from linux recipe" >> ${B}/.config
	echo "CONFIG_LOCALVERSION="\"${LINUX_VERSION_EXTENSION}\" >> ${B}/.config
}

python do_kernel_configcheck() {
    import re, string, sys

    bb.plain("NOTE: validating kernel config, see log.do_kernel_configcheck for details")

    # if KMETA isn't set globally by a recipe using this routine, we need to
    # set the default to 'meta'. Otherwise, kconf_check is not passed a valid
    # meta-series for processing
    kmeta = d.getVar( "KMETA", True ) or "meta"
    if not os.path.exists(kmeta):
        kmeta = "." + kmeta

    pathprefix = "export PATH=%s:%s; " % (d.getVar('PATH', True), "${S}/scripts/util/")
    cmd = d.expand("cd ${S}; kconf_check -config- %s/meta-series ${S} ${B}" % kmeta)
    ret, result = oe.utils.getstatusoutput("%s%s" % (pathprefix, cmd))

    config_check_visibility = d.getVar( "KCONF_AUDIT_LEVEL", True ) or 1
    if config_check_visibility == 1:
        bb.debug( 1, "%s" % result )
    else:
        bb.note( "%s" % result )
}

# Ensure that the branches (BSP and meta) are on the locations specified by
# their SRCREV values. If they are NOT on the right commits, the branches
# are corrected to the proper commit.
do_validate_branches() {
	cd ${S}
	export KMETA=${KMETA}

	machine_branch="${@ get_machine_branch(d, "${KBRANCH}" )}"

	set +e
	# if SRCREV is AUTOREV it shows up as AUTOINC there's nothing to
	# check and we can exit early
	if [ "${SRCREV_machine}" = "AUTOINC" ] || "${SRCREV_machine}" = "INVALID" ] ||
	   [ "${SRCREV_machine}" = "" ]; then
		return
	fi

	# If something other than the default branch was requested, it must
	# exist in the tree, and it's a hard error if it wasn't
	git show-ref --quiet --verify -- "refs/heads/${machine_branch}"
	if [ $? -eq 1 ]; then
		if [ -n "${KBRANCH_DEFAULT}" ] && 
                      [ "${machine_branch}" != "${KBRANCH_DEFAULT}" ]; then
			echo "ERROR: branch ${machine_branch} was set for kernel compilation, "
			echo "       but it does not exist in the kernel repository."
			echo "       Check the value of KBRANCH and ensure that it describes"
			echo "       a valid banch in the source kernel repository"
			exit 1
		fi
	fi

	if [ -z "${SRCREV_machine}" ]; then
		target_branch_head="${SRCREV}"
	else
	 	target_branch_head="${SRCREV_machine}"
	fi

	# $SRCREV could have also been AUTOINC, so check again
	if [ "${target_branch_head}" = "AUTOINC" ]; then
		return
	fi

	ref=`git show ${target_branch_head} 2>&1 | head -n1 || true`
	if [ "$ref" = "fatal: bad object ${target_meta_head}" ]; then
	    echo "ERROR ${target_branch_head} is not a valid commit ID."
	    echo "The kernel source tree may be out of sync"
	    exit 1
	fi

	containing_branches=`git branch --contains $target_branch_head | sed 's/^..//'`
	if [ -z "$containing_branches" ]; then
		echo "ERROR: SRCREV was set to \"$target_branch_head\", but no branches"
		echo "       contain this commit"
		exit 1
	fi

	# force the SRCREV in each branch that contains the specified
	# SRCREV (if it isn't the current HEAD of that branch)
	git checkout -q master
	for b in $containing_branches; do
		branch_head=`git show-ref -s --heads ${b}`		
		if [ "$branch_head" != "$target_branch_head" ]; then
			echo "[INFO] Setting branch $b to ${target_branch_head}"
			if [ "$b" = "master" ]; then
				git reset --hard $target_branch_head > /dev/null
			else
				git branch -D $b > /dev/null
				git branch $b $target_branch_head > /dev/null
			fi
		fi
	done

	## KMETA branch validation
 	meta_head=`git show-ref -s --heads ${KMETA}`
 	target_meta_head="${SRCREV_meta}"
	git show-ref --quiet --verify -- "refs/heads/${KMETA}"
	if [ $? -eq 1 ]; then
		return
	fi

	if [ "${target_meta_head}" = "AUTOINC" ]; then
		return
	fi

	if [ "$meta_head" != "$target_meta_head" ]; then
		ref=`git show ${target_meta_head} 2>&1 | head -n1 || true`
		if [ "$ref" = "fatal: bad object ${target_meta_head}" ]; then
			echo "ERROR ${target_meta_head} is not a valid commit ID"
			echo "The kernel source tree may be out of sync"
			exit 1
		else
			echo "[INFO] Setting branch ${KMETA} to ${target_meta_head}"
			git branch -m ${KMETA} ${KMETA}-orig
			git checkout -q -b ${KMETA} ${target_meta_head}
			if [ $? -ne 0 ];then
				echo "ERROR: could not checkout ${KMETA} branch from known hash ${target_meta_head}"
				exit 1
			fi
		fi
	fi

	git show-ref --quiet --verify -- "refs/heads/${machine_branch}"
	if [ $? -eq 0 ]; then
		# restore the branch for builds
		git checkout -q -f ${machine_branch}
        else
	        git checkout -q master
	fi
}

# Many scripts want to look in arch/$arch/boot for the bootable
# image. This poses a problem for vmlinux based booting. This 
# task arranges to have vmlinux appear in the normalized directory
# location.
do_kernel_link_vmlinux() {
	if [ ! -d "${B}/arch/${ARCH}/boot" ]; then
		mkdir ${B}/arch/${ARCH}/boot
	fi
	cd ${B}/arch/${ARCH}/boot
	ln -sf ../../../vmlinux
}

OE_TERMINAL_EXPORTS += "GUILT_BASE KBUILD_OUTPUT"
GUILT_BASE = "meta"
KBUILD_OUTPUT = "${B}"
