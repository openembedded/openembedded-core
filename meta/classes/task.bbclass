# Task packages are only used to pull in other packages
# via their dependencies. They are empty.
ALLOW_EMPTY = "1"

# By default, only the task package itself is in PACKAGES.
# -dbg and -dev flavours are handled by the anonfunc below.
# This means that task recipes used to build multiple task
# packages have to modify PACKAGES after inheriting task.bbclass.
PACKAGES = "${PN}"

# By default, task packages do not depend on a certain architecture.
# Only if dependencies are modified by MACHINE_FEATURES, packages
# need to be set to MACHINE_ARCH after inheriting task.bbclass
PACKAGE_ARCH = "all"

# This automatically adds -dbg and -dev flavours of all PACKAGES
# to the list. Their dependencies (RRECOMMENDS) are handled as usual
# by package_depchains in a following step.
python () {
    packages = bb.data.getVar('PACKAGES', d, 1).split()
    genpackages = []
    for pkg in packages:
        for postfix in ['-dbg', '-dev']:
            genpackages.append(pkg+postfix)
    bb.data.setVar('PACKAGES', ' '.join(packages+genpackages), d)
}

