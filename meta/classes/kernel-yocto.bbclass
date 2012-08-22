S = "${WORKDIR}/linux"

# remove tasks that modify the source tree in case externalsrc is inherited
SRCTREECOVEREDTASKS += "do_kernel_link_vmlinux do_kernel_configme do_validate_branches do_kernel_configcheck do_kernel_checkout do_patch"

# returns local (absolute) path names for all valid patches in the
# src_uri
def find_patches(d):
	patches=src_patches(d)
	patch_list=[]
	for p in patches:
	    _, _, local, _, _, _ = bb.decodeurl(p)
 	    patch_list.append(local)

	return patch_list

# returns all the elements from the src uri that are .scc files
def find_sccs(d):
	sources=src_patches(d, True)
	sources_list=[]
	for s in sources:
		base, ext = os.path.splitext(os.path.basename(s))
		if ext and ext in ('.scc' '.cfg'):
			sources_list.append(s)
		elif base and base in 'defconfig':
			sources_list.append(s)

	return sources_list

# this is different from find_patches, in that it returns a colon separated
# list of <patches>:<subdir> instead of just a list of patches
def find_urls(d):
	patches=src_patches(d)
	fetch = bb.fetch2.Fetch([], d)
	patch_list=[]
	for p in patches:
		_, _, local, _, _, _ = bb.decodeurl(p)
		for url in fetch.urls:
			urldata = fetch.ud[url]
			if urldata.localpath == local:
				patch_list.append(local+':'+urldata.path)

        return patch_list


do_patch() {
	cd ${S}
	if [ -f ${WORKDIR}/defconfig ]; then
	    defconfig=${WORKDIR}/defconfig
	fi

	# if kernel tools are available in-tree, they are preferred
	# and are placed on the path before any external tools. Unless
	# the external tools flag is set, in that case we do nothing.
	if [ -f "${S}/scripts/util/configme" ]; then
		if [ -z "${EXTERNAL_KERNEL_TOOLS}" ]; then
			PATH=${S}/scripts/util:${PATH}
		fi
	fi

	kbranch=${KBRANCH}
	if [ -n "${YOCTO_KERNEL_EXTERNAL_BRANCH}" ]; then
           # switch from a generic to a specific branch
           kbranch=${YOCTO_KERNEL_EXTERNAL_BRANCH}
	fi

	# simply ensures that a branch of the right name has been created
	if [ -n "${YOCTO_KERNEL_META_DATA}" ]; then
		createme_flags="--disable-meta-gen"
	fi
	createme ${createme_flags} ${ARCH} ${kbranch} ${defconfig}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not create ${kbranch}"
		exit 1
	fi

	sccs="${@" ".join(find_sccs(d))}"
	patches="${@" ".join(find_patches(d))}"

	set +e
	# add any explicitly referenced features onto the end of the feature
	# list that is passed to the kernel build scripts.
	if [ -n "${KERNEL_FEATURES}" ]; then
		for feat in ${KERNEL_FEATURES}; do
			addon_features="$addon_features --feature $feat"
		done
	fi

	# updates or generates the target description
	updateme --branch ${kbranch} -DKDESC=${KMACHINE}:${LINUX_KERNEL_TYPE} \
                           ${addon_features} ${ARCH} ${KMACHINE} ${sccs} ${patches}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not update ${kbranch}"
		exit 1
	fi

	# executes and modifies the source tree as required
	patchme ${kbranch}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not modify ${kbranch}"
		exit 1
	fi
}

do_kernel_checkout() {
	set +e

	# A linux yocto SRC_URI should use the bareclone option. That
	# ensures that all the branches are available in the WORKDIR version
	# of the repository. If it wasn't passed, we should detect it, and put
	# out a useful error message
	if [ -d "${WORKDIR}/git/" ] && [ -d "${WORKDIR}/git/.git" ]; then
		# we build out of {S}, so ensure that ${S} is clean and present
		rm -rf ${S}
		mkdir -p ${S}/.git

		echo "WARNING. ${WORKDIR}/git is not a bare clone."
		echo "Ensure that the SRC_URI includes the 'bareclone=1' option."
		
		# we can fix up the kernel repository, but at the least the meta
		# branch must be present. The machine branch may be created later.
		mv ${WORKDIR}/git/.git ${S}
		rm -rf ${WORKDIR}/git/
		cd ${S}
		if [ -n "${YOCTO_KERNEL_META_DATA}" ] && [ -n "${KMETA}" ]; then
			git branch -a | grep -q ${KMETA}
			if [ $? -ne 0 ]; then
				echo "ERROR. The branch '${KMETA}' is required and was not"
				echo "found. Ensure that the SRC_URI points to a valid linux-yocto"
				echo "kernel repository"
				exit 1
			fi
		fi
	 	if [ -z "${YOCTO_KERNEL_EXTERNAL_BRANCH}" ] && [ -n "${KBRANCH}" ] ; then
			git branch -a | grep -q ${KBRANCH}
			if [ $? -ne 0 ]; then
				echo "ERROR. The branch '${KBRANCH}' is required and was not"
				echo "found. Ensure that the SRC_URI points to a valid linux-yocto"
				echo "kernel repository"
				exit 1
			fi
		fi
	fi
	if [ -d "${WORKDIR}/git/" ] && [ ! -d "${WORKDIR}/git/.git" ]; then
		# we build out of {S}, so ensure that ${S} is clean and present
		rm -rf ${S}
		mkdir -p ${S}/.git

		mv ${WORKDIR}/git/* ${S}/.git
		rm -rf ${WORKDIR}/git/
		cd ${S}	
		git config core.bare false
	fi
	# end debare

	# convert any remote branches to local tracking ones
	for i in `git branch -a | grep remotes | grep -v HEAD`; do
		b=`echo $i | cut -d' ' -f2 | sed 's%remotes/origin/%%'`;
		git show-ref --quiet --verify -- "refs/heads/$b"
		if [ $? -ne 0 ]; then
			git branch $b $i > /dev/null
		fi
	done

	# Create a working tree copy of the kernel by checkout out a branch
	git show-ref --quiet --verify -- "refs/heads/${KBRANCH}"
	if [ $? -eq 0 ]; then
		# checkout and clobber and unimportant files
		git checkout -f ${KBRANCH}
	else
		echo "Not checking out ${KBRANCH}, it will be created later"
		git checkout -f master
	fi
}
do_kernel_checkout[dirs] = "${S}"

addtask kernel_checkout before do_patch after do_unpack

do_kernel_configme[dirs] = "${CCACHE_DIR} ${S} ${B}"
do_kernel_configme() {
	echo "[INFO] doing kernel configme"

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
	configme ${configmeflags} --reconfig --output ${B} ${KBRANCH} ${KMACHINE}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not configure ${KMACHINE}-${LINUX_KERNEL_TYPE}"
		exit 1
	fi
	
	echo "# Global settings from linux recipe" >> ${B}/.config
	echo "CONFIG_LOCALVERSION="\"${LINUX_VERSION_EXTENSION}\" >> ${B}/.config
}

python do_kernel_configcheck() {
    import bb, re, string, sys, commands

    bb.plain("NOTE: validating kernel configuration")

    pathprefix = "export PATH=%s:%s; " % (d.getVar('PATH', True), "${S}/scripts/util/")
    cmd = d.expand("cd ${B}/..; kconf_check -config- ${B} ${S} ${B} ${KBRANCH}")
    ret, result = commands.getstatusoutput("%s%s" % (pathprefix, cmd))

    bb.plain( "%s" % result )
}

# Ensure that the branches (BSP and meta) are on the locations specified by
# their SRCREV values. If they are NOT on the right commits, the branches
# are corrected to the proper commit.
do_validate_branches() {
	cd ${S}
	export KMETA=${KMETA}

	set +e
	# if SRCREV is AUTOREV it shows up as AUTOINC there's nothing to
	# check and we can exit early
	if [ "${SRCREV_machine}" = "AUTOINC" ]; then
		return
	fi

	# If something other than the default branch was requested, it must
	# exist in the tree, and it's a hard error if it wasn't
	git show-ref --quiet --verify -- "refs/heads/${KBRANCH}"
	if [ $? -eq 1 ]; then
		if [ -n "${KBRANCH_DEFAULT}" ] && 
                      [ "${KBRANCH}" != "${KBRANCH_DEFAULT}" ]; then
			echo "ERROR: branch ${KBRANCH} was set for kernel compilation, "
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

	containing_branches=`git branch --contains $target_branch_head | sed 's/^..//'`
	if [ -z "$containing_branches" ]; then
		echo "ERROR: SRCREV was set to \"$target_branch_head\", but no branches"
		echo "       contain this commit"
		exit 1
	fi
	ref=`git show ${target_branch_head} 2>&1 | head -n1 || true`
	if [ "$ref" = "fatal: bad object ${target_meta_head}" ]; then
	    echo "ERROR ${target_branch_head} is not a valid commit ID."
	    echo "The kernel source tree may be out of sync"
	    exit 1
	fi

	# force the SRCREV in each branch that contains the specified
	# SRCREV (if it isn't the current HEAD of that branch)
	git checkout -q master
	for b in $containing_branches; do
		branch_head=`git show-ref -s --heads ${b}`		
		if [ "$branch_head" != "$target_branch_head" ]; then
			echo "[INFO] Setting branch $b to ${target_branch_head}"
			if [ "$b" == "master" ]; then
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

	git show-ref --quiet --verify -- "refs/heads/${KBRANCH}"
	if [ $? -eq 0 ]; then
		# restore the branch for builds
		git checkout -q -f ${KBRANCH}
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


