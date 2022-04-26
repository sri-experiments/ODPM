//
//  ViewController.swift
//  ODPM_iOS
//
//  Created by srivats venkataraman on 3/25/22.
//

import UIKit

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    let context = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
    
    let tableView: UITableView = {
        let table = UITableView()
        table.register(UITableViewCell.self, forCellReuseIdentifier: "cell")
        return table
    }()

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        title = "ODPM"
        view.addSubview(tableView)
        getAllPwds()
        tableView.delegate = self
        tableView.dataSource = self
        tableView.frame = view.bounds
        
//        navigationItem.rightBarButtonItem = UIBarButtonItem(barButtonSystemItem: .add, target: self, action: #selector(didTapAdd))
        
        navigationItem.rightBarButtonItems = [
            UIBarButtonItem(title: "Info", style: .plain, target: self, action: #selector(didInfoTap)),
            UIBarButtonItem(barButtonSystemItem: .add, target: self, action: #selector(didTapAdd)),
        ]
        
    }
    
    @objc private func didInfoTap(){
        
    }
    
    @objc private func didTapAdd(){
        let alert = UIAlertController(title: "New item", message: "Enter New Site", preferredStyle: .alert)
        alert.addTextField{
            (siteNameTextField) in siteNameTextField.placeholder = "Site Name"
        }
        alert.addTextField{
            (sitePwdTextField) in sitePwdTextField.placeholder = "Site Password"
            sitePwdTextField.isSecureTextEntry = true
        }
        alert.addAction(UIAlertAction(title: "Save", style: .default,
                                      handler: {[weak self] _ in
            
            let siteNameField = alert.textFields![0] as UITextField
            guard let siteName = siteNameField.text, !siteName.isEmpty else{
                return
            }
            
            
            let sitePasswordField = alert.textFields![1] as UITextField
            guard let sitePwd = sitePasswordField.text, !sitePwd.isEmpty else{
                return
            }
            
            self?.createPwd(siteName: siteName, password: sitePwd)
        }))
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel))
        
        present(alert, animated: true)
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int{
        return models.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let model = models[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell",
        for: indexPath)
        cell.textLabel?.text = model.siteName
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let item = models[indexPath.row]
        let sheet = UIAlertController(title: "Options", message: nil, preferredStyle: .actionSheet)
        
        // view pwd
        sheet.addAction(UIAlertAction(title: "View", style: .default,
                                      handler: {_ in
            let alert = UIAlertController(title: item.siteName, message: "Password: " + (item.password ?? "Password Not Available"), preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel))
            self.present(alert, animated: true)
        }))
        
        // edit pwd
        sheet.addAction(UIAlertAction(title: "Edit", style: .default,
                                      handler: {_ in
            let alert = UIAlertController(title: "Edit item", message: item.siteName, preferredStyle: .alert)
            alert.addTextField{
                (sitePwdTextField) in sitePwdTextField.placeholder = "New Password"
                sitePwdTextField.isSecureTextEntry = true
            }
            alert.addAction(UIAlertAction(title: "Save", style: .default,
                                          handler: {[weak self] _ in
                
                let sitePasswordField = alert.textFields![0] as UITextField
                guard let sitePwd = sitePasswordField.text, !sitePwd.isEmpty else{
                    return
                }
                
                self?.updateItem(item: item, newPwd: sitePwd)
            }))
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel))
            
            self.present(alert, animated: true)
        }))
        
        // share pwd
        sheet.addAction(UIAlertAction(title: "Share", style: .default,
                                      handler: {_ in
            self.shareSheet(siteName: item.siteName ?? "",
                            pwd: item.password ?? "Password Not set")
        }))
        
        // delete pwd
        sheet.addAction(UIAlertAction(title: "Delete", style: .destructive,
                                      handler: {[weak self] _ in
            self?.deletePwd(item: item)
        }))
        
        // closes the bottom sheet
        sheet.addAction(UIAlertAction(title: "Cancel", style: .cancel,
                                     handler: nil))
        present(sheet, animated: true)
    }
    
    @objc private func shareSheet(siteName: String, pwd: String){
        let shareSheetVC = UIActivityViewController(activityItems: [siteName, pwd], applicationActivities: nil)
        
        present(shareSheetVC, animated: true)
    }
    
    private var models = [PasswordItem]()
    
    // core data
    func getAllPwds() {
        do{
            models = try context.fetch(PasswordItem.fetchRequest())
            DispatchQueue.main.async {
                self.tableView.reloadData()
            }
        }
        catch{
            // catch error
        }
    }
    
    func createPwd(siteName: String, password: String){
        let newPwd = PasswordItem(context: context)
        newPwd.siteName = siteName
        newPwd.password = password
        newPwd.createdAt = Date()
        
        do{
            try context.save()
            getAllPwds()
        }
        catch{}
    }
    
    func deletePwd(item: PasswordItem){
        context.delete(item)
        do{
            try context.save()
            getAllPwds()
        }
        catch{}
    }
    
    func updateItem(item: PasswordItem, newPwd: String){
        item.password = newPwd
        do{
            try context.save()
            getAllPwds()
        }
        catch{}
    }

}

