# kubectl cli commands

## Os comandos abaixo devem ser executados na pasta do Kubernetes (`cd kubernetes`)]

1. Para aplicar nosso Secret

```
kubectl apply -f secrets.yaml
```

2. Para aplicar nosso ConfigMap

```
kubectl apply -f config-db.yaml
```

3. Para aplicar nosso PersistentVolume

```
kubectl apply -f pv-db.yaml
```

4. Para aplicar nosso PersistentVolumeClaim

```
kubectl apply -f pvc-db.yaml
```

5. Para aplicar nosso Deployment do banco de dados

```
kubectl apply -f deployment-db.yaml
```

6. Para aplicar nosso Service do banco de dados (aguarde o STATUS como "Running")

```
kubectl apply -f service-db.yaml
```

7. Para encaminhar a porta do serviço do banco de dados Postgres 5432 para nossa porta local 5432, para que possamos conectá-lo no DBeaver

```
kubectl port-forward svc/postgres 5432:5432
```

8. Para aplicar nosso Deployment do aplicativo

```
kubectl apply -f deployment-app.yaml
```

9. Para aplicar nosso Service do aplicativo

```
kubectl apply -f service-app.yaml
```

10. Para aplicar nosso HPA (Horizontal Pod Autoscaler) para autoescalar nossos pods

```
kubectl apply -f hpa-app.yaml
```

11. Para encaminhar a porta do serviço fiap-video-processor 8080 para nossa porta local 8080, para enviar requisições no Postman (que está configurado para a porta 8080)

```
kubectl port-forward svc/fiap-video-processor 8080:8080
```

Para gerar um arquivo de configmap para o Postgres com base em nossos SQL scripts (**execute na pasta raiz**). Só precisa rodar de novo caso os scripts sejam atualizados

```
kubectl create configmap postgres-init-scripts --from-file=src/main/resources/sql_scripts/ -o yaml --dry-run=client > kubernetes/config-db.yaml
```
