# Quick Start: Push to GitHub

## ✅ Status: Git Repository Ready!

Your project has been initialized with Git and all files have been committed.

---

## 🎯 3 Simple Steps to Push to GitHub

### 🔹 Step 1: Create Repository on GitHub

1. Open your browser and go to: **https://github.com/new**

2. Fill in the form:
   ```
   Repository name: sisdat-web
   Description: Sistema de Dados e Análise Técnica - Web Application
   Visibility: ○ Public  or  ○ Private (your choice)
   
   ⚠️ IMPORTANT: Leave these UNCHECKED:
   ☐ Add a README file
   ☐ Add .gitignore
   ☐ Choose a license
   ```

3. Click the green **"Create repository"** button

---

### 🔹 Step 2: Copy Your GitHub Username

After creating the repository, you'll be redirected to a page with setup instructions.

Your repository URL will look like:
```
https://github.com/YOUR_USERNAME/sisdat-web
```

**Copy YOUR_USERNAME** from this URL.

---

### 🔹 Step 3: Run These Commands

Open a terminal (Command Prompt or PowerShell) and run:

```cmd
cd C:\Projetos\sisdat-web

rem Replace YOUR_USERNAME with your actual GitHub username
git remote add origin https://github.com/YOUR_USERNAME/sisdat-web.git

rem Push your code
git push -u origin master
```

**Example** (if your username is "johndoe"):
```cmd
cd C:\Projetos\sisdat-web
git remote add origin https://github.com/johndoe/sisdat-web.git
git push -u origin master
```

---

## 🔐 Authentication

When you run `git push`, you'll be asked for credentials:

### ✅ Option 1: Personal Access Token (Recommended)

1. While the git push command is waiting, go to: https://github.com/settings/tokens
2. Click **"Generate new token (classic)"**
3. Give it a name: `sisdat-web-token`
4. Select scope: **☑ repo** (full control of private repositories)
5. Click **"Generate token"**  at the bottom
6. **COPY the token** (you won't see it again!)
7. Paste it as your password in the terminal

### Option 2: GitHub CLI (if installed)

```cmd
gh auth login
```

---

## ✅ Success!

After successfully pushing, you'll see:
```
Enumerating objects: 579, done.
Counting objects: 100% (579/579), done.
...
To https://github.com/YOUR_USERNAME/sisdat-web.git
 * [new branch]      master -> master
Branch 'master' set up to track remote branch 'master' from 'origin'.
```

Visit your repository at:
```
https://github.com/YOUR_USERNAME/sisdat-web
```

---

## 📋 What's Next?

After successfully pushing:

1. **Restore Database** - Follow instructions in `RESTORE_DATABASE.md`
2. **Test Application** - Access at http://localhost:8080
3. **Invite Collaborators** - Go to Settings → Collaborators on GitHub

---

## 🆘 Common Issues

### ❌ Error: "remote origin already exists"
**Solution:**
```cmd
git remote remove origin
git remote add origin https://github.com/YOUR_USERNAME/sisdat-web.git
git push -u origin master
```

### ❌ Error: "Authentication failed"
**Solution:** Use a Personal Access Token (see Option 1 above) instead of your GitHub password

### ❌ Error: "failed to push some refs"
**Solution:**
```cmd
git pull --rebase origin master
git push -u origin master
```

---

## 📞 Need Help?

Full detailed guide: See `GITHUB_SETUP.md` in this directory

---

**Your code is ready to be pushed! Just follow the 3 steps above.** 🚀
