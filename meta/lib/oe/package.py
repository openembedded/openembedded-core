
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
