def runstrip(arg):
    # Function to strip a single file, called from split_and_strip_files below
    # A working 'file' (one which works on the target architecture)
    #
    # The elftype is a bit pattern (explained in split_and_strip_files) to tell
    # us what type of file we're processing...
    # 4 - executable
    # 8 - shared library
    # 16 - kernel module

    import commands, stat, subprocess

    (file, elftype, strip) = arg

    newmode = None
    if not os.access(file, os.W_OK) or os.access(file, os.R_OK):
        origmode = os.stat(file)[stat.ST_MODE]
        newmode = origmode | stat.S_IWRITE | stat.S_IREAD
        os.chmod(file, newmode)

    extraflags = ""

    # kernel module    
    if elftype & 16:
        extraflags = "--strip-debug --remove-section=.comment --remove-section=.note --preserve-dates"
    # .so and shared library
    elif ".so" in file and elftype & 8:
        extraflags = "--remove-section=.comment --remove-section=.note --strip-unneeded"
    # shared or executable:
    elif elftype & 8 or elftype & 4:
        extraflags = "--remove-section=.comment --remove-section=.note"

    stripcmd = "'%s' %s '%s'" % (strip, extraflags, file)
    bb.debug(1, "runstrip: %s" % stripcmd)

    ret = subprocess.call(stripcmd, shell=True)

    if newmode:
        os.chmod(file, origmode)

    if ret:
        bb.error("runstrip: '%s' strip command failed" % stripcmd)

    return


def file_translate(file):
    ft = file.replace("@", "@at@")
    ft = ft.replace(" ", "@space@")
    ft = ft.replace("\t", "@tab@")
    ft = ft.replace("[", "@openbrace@")
    ft = ft.replace("]", "@closebrace@")
    ft = ft.replace("_", "@underscore@")
    return ft

def filedeprunner(arg):
    import re

    (pkg, pkgfiles, rpmdeps, pkgdest) = arg
    provides = {}
    requires = {}

    r = re.compile(r'[<>=]+ +[^ ]*')

    def process_deps(pipe, pkg, pkgdest, provides, requires):
        for line in pipe:
            f = line.split(" ", 1)[0].strip()
            line = line.split(" ", 1)[1].strip()

            if line.startswith("Requires:"):
                i = requires
            elif line.startswith("Provides:"):
                i = provides
            else:
                continue

            file = f.replace(pkgdest + "/" + pkg, "")
            file = file_translate(file)
            value = line.split(":", 1)[1].strip()
            value = r.sub(r'(\g<0>)', value)

            if value.startswith("rpmlib("):
                continue
            if value == "python":
                continue
            if file not in i:
                i[file] = []
            i[file].append(value)

        return provides, requires

    dep_pipe = os.popen(rpmdeps + " " + " ".join(pkgfiles))

    provides, requires = process_deps(dep_pipe, pkg, pkgdest, provides, requires)

    return (pkg, provides, requires)
